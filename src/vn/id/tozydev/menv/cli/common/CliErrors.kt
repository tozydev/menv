package vn.id.tozydev.menv.cli.common

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.ProgramResult

context(cmd: CliktCommand)
internal fun <T> Result<T>.ensureNotFail(): T {
    return getOrElse {
        cmd.echo("menv: ${it.message}", err = true)
        throw ProgramResult(2)
    }
}
