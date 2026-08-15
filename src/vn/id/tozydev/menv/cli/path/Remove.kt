package vn.id.tozydev.menv.cli.path

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.arguments.argument
import vn.id.tozydev.menv.cli.common.ensureNotFail
import vn.id.tozydev.menv.model.PathList
import vn.id.tozydev.menv.model.Scope
import vn.id.tozydev.menv.service.EnvironmentVariables
import vn.id.tozydev.menv.service.PathService

class Remove : CliktCommand() {
    override fun help(context: Context): String =
        "Remove a directory from the user PATH (case-insensitive)."

    private val dir by argument("DIR", help = "Directory to remove.")

    override fun run() {
        val service = PathService(EnvironmentVariables(Scope.USER))
        val result = service.remove(dir).ensureNotFail()
        if (!result.changed) {
            echo("not found: ${PathList.normalizeDir(dir)}")
            return
        }
        echo("removed: ${PathList.normalizeDir(dir)}")
    }
}
