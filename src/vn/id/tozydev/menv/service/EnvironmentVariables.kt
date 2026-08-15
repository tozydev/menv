@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package vn.id.tozydev.menv.service

import platform.windows.HKEY
import platform.windows.HKEY_CURRENT_USER
import platform.windows.HKEY_LOCAL_MACHINE
import vn.id.tozydev.menv.model.EnvVar
import vn.id.tozydev.menv.model.Scope
import vn.id.tozydev.menv.native.Registry
import vn.id.tozydev.menv.native.broadcastSettingChange
import vn.id.tozydev.menv.native.expandWindows

private const val PATH_NAME = "PATH"

class EnvironmentVariables(private val scope: Scope) {

    private val hive: HKEY?
        get() = if (scope == Scope.USER) HKEY_CURRENT_USER else HKEY_LOCAL_MACHINE

    private val subKey: String
        get() = if (scope == Scope.USER) Registry.USER_ENV_KEY else Registry.SYSTEM_ENV_KEY

    fun list(): Result<List<EnvVar>> = Registry.listKey(hive, subKey)

    fun get(name: String): Result<EnvVar?> = Registry.getValue(hive, subKey, name)

    fun getPath(): Result<String> = get(PATH_NAME).map { it?.value ?: "" }

    fun setPath(value: String): Result<Unit> = set(PATH_NAME, value)

    fun set(name: String, value: String): Result<Unit> =
        Registry.setValue(hive, subKey, name, value).onSuccess { broadcastSettingChange() }

    fun unset(name: String): Result<Boolean> =
        Registry.unsetValue(hive, subKey, name).onSuccess { deleted ->
            if (deleted) broadcastSettingChange()
        }

    fun expand(value: String): String = expandWindows(value)
}
