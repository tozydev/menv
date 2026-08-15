package vn.id.tozydev.menv.cli

import com.github.ajalt.clikt.core.Abort
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import vn.id.tozydev.menv.cli.common.ensureNotFail
import vn.id.tozydev.menv.cli.common.scopeOption
import vn.id.tozydev.menv.service.EnvironmentVariables

class Get : CliktCommand() {
    override fun help(context: Context): String = "Print the value of a single variable."

    private val scope by scopeOption("Which environment to read.")
    private val raw by
        option("--raw", help = "Print the stored value without expanding %VARS%.").flag()
    private val name by argument("NAME", help = "Variable name (case-insensitive).")

    override fun run() {
        val env = EnvironmentVariables(scope)
        val v = env.get(name).ensureNotFail()
        if (v == null) {
            echo("menv: variable '$name' not found in the $scope environment", err = true)
            throw Abort()
        }
        echo(if (raw) v.value else env.expand(v.value))
    }
}
