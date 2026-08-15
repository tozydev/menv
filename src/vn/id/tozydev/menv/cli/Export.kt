package vn.id.tozydev.menv.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.core.ProgramResult
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.choice
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.writeString
import vn.id.tozydev.menv.cli.common.ensureNotFail
import vn.id.tozydev.menv.model.Scope
import vn.id.tozydev.menv.service.EnvironmentVariables
import vn.id.tozydev.menv.service.ExportFormat
import vn.id.tozydev.menv.service.ExportService

class Export : CliktCommand() {
    override fun help(context: Context): String =
        "Export the user environment in a reusable format."

    private val format by
        option("--format", help = "Output format.")
            .choice("dotenv", "powershell", "cmd")
            .default("dotenv")
    private val output by option("-o", "--output", help = "Write to a file instead of stdout.")
    private val expanded by
        option("--expanded", help = "Expand %VARS% in values before exporting.").flag()

    override fun run() {
        val env = EnvironmentVariables(Scope.USER)
        val vars = env.list().ensureNotFail()
        val entries =
            vars
                .sortedBy { it.name.lowercase() }
                .map { v -> v.name to if (expanded) env.expand(v.value) else v.value }
        val text = ExportService.format(entries, ExportFormat.fromId(format))
        val out = output
        if (out != null) {
            try {
                SystemFileSystem.sink(Path(out), append = false).buffered().use {
                    it.writeString(text)
                }
                echo("exported ${entries.size} variables to $out")
            } catch (e: Exception) {
                echo("menv: cannot write '$out': ${e.message}", err = true)
                throw ProgramResult(2)
            }
        } else {
            echo(text.trimEnd('\n'))
        }
    }
}
