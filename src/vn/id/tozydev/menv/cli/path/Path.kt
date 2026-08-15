package vn.id.tozydev.menv.cli.path

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import vn.id.tozydev.menv.cli.common.ensureNotFail
import vn.id.tozydev.menv.model.PathList
import vn.id.tozydev.menv.model.Scope
import vn.id.tozydev.menv.service.EnvironmentVariables

class Path : CliktCommand() {
    override fun help(context: Context): String = "Show the user PATH, one entry per line."

    override fun run() {
        val entries =
            EnvironmentVariables(Scope.USER).getPath().map { PathList(it).entries }.ensureNotFail()
        entries.forEach(::echo)
    }
}
