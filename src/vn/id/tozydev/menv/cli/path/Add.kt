package vn.id.tozydev.menv.cli.path

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import vn.id.tozydev.menv.cli.common.ensureNotFail
import vn.id.tozydev.menv.model.Scope
import vn.id.tozydev.menv.service.EnvironmentVariables
import vn.id.tozydev.menv.service.PathService

class Add : CliktCommand() {
    override fun help(context: Context): String =
        "Add a directory to the user PATH (deduplicated, case-insensitive)."

    private val prepend by
        option("--prepend", help = "Insert at the beginning instead of the end.").flag()
    private val dir by argument("DIR", help = "Directory to add.")

    override fun run() {
        val service = PathService(EnvironmentVariables(Scope.USER))
        val result = service.add(dir, prepend).ensureNotFail()
        if (!result.changed) {
            echo("already present: ${result.normalized}")
            return
        }
        echo("added: ${result.normalized}")
    }
}
