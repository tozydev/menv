package vn.id.tozydev.menv.cli

import com.github.ajalt.clikt.core.Abort
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.arguments.argument
import vn.id.tozydev.menv.cli.common.ensureNotFail
import vn.id.tozydev.menv.cli.common.scopeOption
import vn.id.tozydev.menv.service.EnvironmentVariables

class Unset : CliktCommand() {
    override fun help(context: Context): String =
        "Remove a variable from the persisted environment."

    private val scope by scopeOption("Which environment to modify.")
    private val name by argument("NAME", help = "Variable name (case-insensitive).")

    override fun run() {
        val deleted = EnvironmentVariables(scope).unset(name).ensureNotFail()
        if (!deleted) {
            echo("menv: variable '$name' not found in the $scope environment", err = true)
            throw Abort()
        }
        echo("unset $name")
    }
}
