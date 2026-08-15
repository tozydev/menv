package vn.id.tozydev.menv.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import vn.id.tozydev.menv.cli.common.ensureNotFail
import vn.id.tozydev.menv.cli.common.scopeOption
import vn.id.tozydev.menv.service.EnvironmentVariables

class List : CliktCommand() {
    override fun help(context: Context): String = "List environment variables (raw stored values)."

    private val scope by scopeOption("Which environment to list.")
    private val verbose by option("--verbose", "-v", help = "Show the registry value type.").flag()

    override fun run() {
        val vars = EnvironmentVariables(scope).list().ensureNotFail()
        for (v in vars.sortedBy { it.name.lowercase() }) {
            if (verbose) echo("${v.name}=${v.value}\t${v.type.display}")
            else echo("${v.name}=${v.value}")
        }
    }
}
