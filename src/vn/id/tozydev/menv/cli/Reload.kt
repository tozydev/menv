package vn.id.tozydev.menv.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import vn.id.tozydev.menv.cli.common.ensureNotFail
import vn.id.tozydev.menv.native.broadcastSettingChange

class Reload : CliktCommand() {
    override fun help(context: Context): String =
        "Broadcast WM_SETTINGCHANGE so new processes see updated variables."

    override fun run() {
        broadcastSettingChange().ensureNotFail()
        echo("broadcast WM_SETTINGCHANGE")
    }
}
