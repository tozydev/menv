package vn.id.tozydev.menv.service

enum class ExportFormat(val id: String) {
    DOTENV("dotenv"),
    POWERSHELL("powershell"),
    CMD("cmd");

    companion object {
        fun fromId(id: String): ExportFormat = entries.firstOrNull { it.id == id } ?: DOTENV
    }
}

object ExportService {

    fun format(entries: List<Pair<String, String>>, format: ExportFormat): String {
        val lines = entries.map { (name, value) -> render(name, value, format) }
        return lines.joinToString("\n") + "\n"
    }

    private fun render(name: String, value: String, format: ExportFormat): String =
        when (format) {
            ExportFormat.DOTENV -> "$name=${quoteDotenv(value)}"
            ExportFormat.POWERSHELL -> "\$env:$name = '${value.replace("'", "''")}'"
            ExportFormat.CMD -> "set $name=${escapeCmd(value)}"
        }

    internal fun quoteDotenv(value: String): String {
        val needsQuotes = value.any { it == ' ' || it == '\t' || it == '#' || it == '"' }
        return if (needsQuotes) "\"" + value.replace("\"", "\\\"") + "\"" else value
    }

    internal fun escapeCmd(value: String): String =
        value.replace("^", "^^").replace(Regex("[&|<>]")) { m -> "^" + m.value }
}
