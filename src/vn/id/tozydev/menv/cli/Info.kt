package vn.id.tozydev.menv.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context

class Info : CliktCommand() {
    override fun help(context: Context): String = "Show information about menv."

    override fun run() {
        echo("menv $MENV_VERSION - Windows environment variable manager (Kotlin/Native)")
        echo()
        echo("Scopes:")
        echo("  user    HKEY_CURRENT_USER\\Environment                    (read/write)")
        echo(
            "  system  HKEY_LOCAL_MACHINE\\SYSTEM\\CurrentControlSet\\...  (read; write requires elevation)"
        )
        echo()
        echo("PATH entries are stored as REG_EXPAND_SZ and edited without expansion.")
    }
}
