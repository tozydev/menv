package vn.id.tozydev.menv

import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands
import vn.id.tozydev.menv.cli.Export
import vn.id.tozydev.menv.cli.Get
import vn.id.tozydev.menv.cli.Info
import vn.id.tozydev.menv.cli.List
import vn.id.tozydev.menv.cli.MenvRoot
import vn.id.tozydev.menv.cli.Reload
import vn.id.tozydev.menv.cli.Set
import vn.id.tozydev.menv.cli.Unset
import vn.id.tozydev.menv.cli.path.Add
import vn.id.tozydev.menv.cli.path.Path
import vn.id.tozydev.menv.cli.path.Remove

fun main(args: Array<String>) =
    MenvRoot()
        .subcommands(
            List(),
            Get(),
            Set(),
            Unset(),
            Path().subcommands(Add(), Remove()),
            Export(),
            Reload(),
            Info(),
        )
        .main(args)
