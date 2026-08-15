package vn.id.tozydev.menv.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.core.UsageError
import com.github.ajalt.clikt.parameters.arguments.argument
import vn.id.tozydev.menv.cli.common.ensureNotFail
import vn.id.tozydev.menv.cli.common.scopeOption
import vn.id.tozydev.menv.model.ENV_NAME_PATTERN
import vn.id.tozydev.menv.model.inferType
import vn.id.tozydev.menv.service.EnvironmentVariables

class Set : CliktCommand() {
    override fun help(context: Context): String = "Set and persist an environment variable."

    private val scope by scopeOption("Which environment to write.")
    private val name by argument("NAME", help = "Variable name (case-insensitive).")
    private val value by
        argument("VALUE", help = "Value to store. %VAR% patterns are stored as REG_EXPAND_SZ.")

    override fun run() {
        if (!ENV_NAME_PATTERN.matches(name)) {
            throw UsageError("invalid variable name '$name'")
        }
        if (value.contains('\u0000')) {
            throw UsageError("value cannot contain a NUL character")
        }
        EnvironmentVariables(scope).set(name, value).ensureNotFail()
        echo("set $name (scope: ${scope.value}, type: ${inferType(value).display})")
    }
}
