@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package vn.id.tozydev.menv.native

import kotlinx.cinterop.ULongVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.wcstr
import platform.windows.HWND_BROADCAST
import platform.windows.SMTO_ABORTIFHUNG
import platform.windows.SendMessageTimeoutW
import platform.windows.WM_SETTINGCHANGE

fun broadcastSettingChange(): Result<Unit> = runCatching {
    memScoped {
        val result = alloc<ULongVar>()
        SendMessageTimeoutW(
            HWND_BROADCAST,
            WM_SETTINGCHANGE.toUInt(),
            0uL,
            "Environment".wcstr.ptr.rawValue.toLong(),
            SMTO_ABORTIFHUNG.toUInt(),
            5000u,
            result.ptr,
        )
    }
}
