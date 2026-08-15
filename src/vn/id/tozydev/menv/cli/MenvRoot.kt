package vn.id.tozydev.menv.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.options.versionOption

class MenvRoot : CliktCommand(name = "menv") {
    init {
        versionOption(MENV_VERSION)
    }

    override fun help(context: Context): String = "Manage Windows environment variables."

    override fun run() = Unit
}
