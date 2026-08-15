package vn.id.tozydev.menv.cli.common

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.choice
import vn.id.tozydev.menv.model.Scope

internal fun CliktCommand.scopeOption(help: String) =
    option("-s", "--scope", help = "$help (default: ${Scope.USER.value})")
        .choice(choices = Scope.entries.associateBy { it.value }, ignoreCase = true)
        .default(Scope.USER)
