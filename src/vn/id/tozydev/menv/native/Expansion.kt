@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package vn.id.tozydev.menv.native

import kotlinx.cinterop.UShortVar
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toKStringFromUtf16
import platform.windows.ExpandEnvironmentStringsW

fun expandWindows(value: String): String = memScoped {
    if (value.isEmpty()) return value
    val required = ExpandEnvironmentStringsW(value, null, 0u)
    if (required == 0u) return value
    val buf = allocArray<UShortVar>(required.toInt())
    ExpandEnvironmentStringsW(value, buf, required)
    buf.toKStringFromUtf16()
}
