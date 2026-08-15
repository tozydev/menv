# 🔧 menv

![GitHub Release](https://img.shields.io/github/v/release/tozydev/menv)
![GitHub License](https://img.shields.io/github/license/tozydev/menv)
![Product: windows/app](https://img.shields.io/badge/product-windows/app-violet?logo=mingww64)

A Windows environment variable manager CLI, written in Kotlin/Native and built with
the [Kotlin Toolchain](https://kotlin-toolchain.org).

It reads and writes environment variables directly in the registry, handles `%VAR%` expansion types, and manages the
user `PATH`.

## ✨ Features

- List, get, set, and unset environment variables.
- Show the user `PATH` variable, one entry per line.
- Export the user environment in a reusable format.
- Broadcast WM_SETTINGCHANGE so new processes see updated variables.

## 📦 Installation

### 🚀 Prebuilt Executable

You can download the executable `menv.exe` from the [releases](https://github.com/tozydev/menv/releases) page.

### From Source

1. Clone the repository:

   ```bash
   git clone https://github.com/tozydev/menv.git
   cd menv
   ```
2. Build the executable:

   ```bash
   ./kotlin build
   ```
3. Execute the executable:

   ```bash
   ./build/tasks/_menv_linkMingwX64Debug/menv.exe
   ```

## 📖 Usage

Use `menv -h` to see the available commands:

```
Usage: menv [<options>] <command> [<args>]...

  Manage Windows environment variables.

Options:
  --version   Show the version and exit
  -h, --help  Show this message and exit

Commands:
  list    List environment variables (raw stored values).
  get     Print the value of a single variable.
  set     Set and persist an environment variable.
  unset   Remove a variable from the persisted environment.
  path    Show the user PATH, one entry per line.
  export  Export the user environment in a reusable format.
  reload  Broadcast WM_SETTINGCHANGE so new processes see updated variables.
  info    Show information about menv.
```

## 📄 License

This project is licensed under the Apache License 2.0 — see the [LICENSE](LICENSE) file for details.

## 🎁 Acknowledgements

I would like to thank the following: projects, libraries, and tools that help make this project possible:

- [Kotlin](https://kotlinglang.org) – The `fun` programming language for modern multiplatform development.
- [Kotlin Toolchain](https://kotlin-toolchain.org) – The toolchain for building Kotlin applications with minimal effort.
- [clikt](https://github.com/ajalt/clikt) – A command-line interface library for Kotlin.
- [kotlinx-io](https://github.com/Kotlin/kotlinx-io) – A multiplatform I/O library for Kotlin.
