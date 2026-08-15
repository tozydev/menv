@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package vn.id.tozydev.menv.native

import kotlinx.cinterop.CPointerVarOf
import kotlinx.cinterop.UByteVar
import kotlinx.cinterop.UIntVar
import kotlinx.cinterop.UShortVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.toKStringFromUtf16
import kotlinx.cinterop.value
import kotlinx.cinterop.wcstr
import platform.windows.HKEY
import platform.windows.RegCloseKey
import platform.windows.RegDeleteValueW
import platform.windows.RegEnumValueW
import platform.windows.RegOpenKeyExW
import platform.windows.RegQueryValueExW
import platform.windows.RegSetValueExW
import vn.id.tozydev.menv.model.EnvType
import vn.id.tozydev.menv.model.EnvVar
import vn.id.tozydev.menv.model.inferType

class RegistryError(message: String) : Throwable(message)

object Registry {

    const val USER_ENV_KEY = "Environment"
    const val SYSTEM_ENV_KEY = "SYSTEM\\CurrentControlSet\\Control\\Session Manager\\Environment"

    private const val KEY_READ = 0x20019u
    private const val KEY_WRITE = 0x20006u
    private const val ERROR_FILE_NOT_FOUND = 2
    private const val ERROR_ACCESS_DENIED = 5
    private const val ERROR_NO_MORE_ITEMS = 259

    fun listKey(hive: HKEY?, subKey: String): Result<List<EnvVar>> =
        withKey(hive, subKey, KEY_READ) { key ->
            val result = mutableListOf<EnvVar>()
            var index = 0u
            while (true) {
                val item = memScoped {
                    val nameBuf = allocArray<UShortVar>(16384)
                    val nameLen = alloc<UIntVar>().apply { value = 16383u }
                    val s =
                        RegEnumValueW(
                            key,
                            index,
                            nameBuf,
                            nameLen.ptr,
                            null,
                            null,
                            null,
                            null,
                        )
                    when (s) {
                        0 -> readValue(key, nameBuf.toKStringFromUtf16())
                        ERROR_NO_MORE_ITEMS -> Result.success<EnvVar?>(null)
                        else ->
                            Result.failure(
                                RegistryError("failed to enumerate registry values: error $s")
                            )
                    }
                }
                    .getOrElse {
                        return@withKey Result.failure(it)
                    }
                if (item == null) return@withKey Result.success(result)
                result.add(item)
                index += 1u
            }
            Result.success(result)
        }

    fun getValue(hive: HKEY?, subKey: String, name: String): Result<EnvVar?> =
        withKey(hive, subKey, KEY_READ) { key -> readValue(key, name) }

    fun setValue(hive: HKEY?, subKey: String, name: String, value: String): Result<Unit> =
        withKey(hive, subKey, KEY_WRITE) { key -> writeValue(key, name, value, inferType(value)) }

    fun unsetValue(hive: HKEY?, subKey: String, name: String): Result<Boolean> =
        withKey(hive, subKey, KEY_WRITE) { key -> deleteValue(key, name) }

    private fun readValue(hKey: HKEY?, name: String): Result<EnvVar?> = memScoped {
        val type = alloc<UIntVar>()
        val size = alloc<UIntVar>()
        var status = RegQueryValueExW(hKey, name, null, type.ptr, null, size.ptr)
        if (status == ERROR_FILE_NOT_FOUND) return Result.success(null)
        if (status != 0) {
            return Result.failure(RegistryError("failed to query '$name': error $status"))
        }
        val typeValue = type.value
        if (typeValue == EnvType.DWORD.regValue) {
            val data = alloc<UIntVar>()
            val cb = alloc<UIntVar>().apply { value = 4u }
            status = RegQueryValueExW(hKey, name, null, null, data.ptr.reinterpret(), cb.ptr)
            if (status != 0) {
                return Result.failure(RegistryError("failed to read '$name': error $status"))
            }
            return Result.success(EnvVar(name, data.value.toString(), EnvType.DWORD))
        }
        val buf = allocArray<UByteVar>((size.value + 2u).toInt())
        val cb = alloc<UIntVar>().apply { value = size.value + 2u }
        status = RegQueryValueExW(hKey, name, null, null, buf, cb.ptr)
        if (status != 0) {
            return Result.failure(RegistryError("failed to read '$name': error $status"))
        }
        Result.success(
            EnvVar(
                name,
                buf.reinterpret<UShortVar>().toKStringFromUtf16(),
                EnvType.fromReg(typeValue),
            )
        )
    }

    private fun writeValue(hKey: HKEY?, name: String, value: String, type: EnvType): Result<Unit> =
        memScoped {
            val bytes = value.wcstr.ptr.reinterpret<UByteVar>()
            val cb = ((value.length + 1) * 2).toUInt()
            val status = RegSetValueExW(hKey, name, 0u, type.regValue, bytes, cb)
            if (status != 0) {
                Result.failure(RegistryError("failed to write '$name': error $status"))
            } else {
                Result.success(Unit)
            }
        }

    private fun deleteValue(hKey: HKEY?, name: String): Result<Boolean> = memScoped {
        when (val status = RegDeleteValueW(hKey, name)) {
            0 -> Result.success(true)
            ERROR_FILE_NOT_FOUND -> Result.success(false)
            else -> Result.failure(RegistryError("failed to delete '$name': error $status"))
        }
    }

    private fun <T> withKey(
        hive: HKEY?,
        subKey: String,
        access: UInt,
        block: (HKEY?) -> Result<T>,
    ): Result<T> = memScoped {
        val phk = alloc<CPointerVarOf<HKEY>>()
        val status = RegOpenKeyExW(hive, subKey, 0u, access, phk.ptr)
        if (status != 0) {
            val hint =
                if (status == ERROR_ACCESS_DENIED) " (access denied; run from an elevated prompt?)"
                else ""
            return Result.failure(
                RegistryError("cannot open registry key '$subKey': error $status$hint")
            )
        }
        try {
            block(phk.value)
        } finally {
            RegCloseKey(phk.value)
        }
    }
}
