package vn.id.tozydev.menv.model

data class AddResult(val raw: String, val changed: Boolean, val normalized: String)

data class RemoveResult(val raw: String, val changed: Boolean)

value class PathList(val raw: String) {

    val entries: List<String>
        get() = raw.split(';').map { it.trim() }.filter { it.isNotEmpty() }

    fun add(dir: String, prepend: Boolean): AddResult {
        val normalized = normalizeDir(dir)
        if (normalized.isEmpty()) return AddResult(raw, false, normalized)
        val current = entries
        if (current.any { it.equals(normalized, ignoreCase = true) }) {
            return AddResult(raw, false, normalized)
        }
        val updated = if (prepend) listOf(normalized) + current else current + normalized
        return AddResult(updated.joinToString(";"), true, normalized)
    }

    fun remove(dir: String): RemoveResult {
        val normalized = normalizeDir(dir)
        val current = entries
        val updated = current.filterNot { it.equals(normalized, ignoreCase = true) }
        return RemoveResult(updated.joinToString(";"), updated.size != current.size)
    }

    companion object {
        private val ROOT_DIR = Regex("^[A-Za-z]:[\\\\/]$")

        fun normalizeDir(dir: String): String {
            var d = dir.trim()
            if (d.endsWith("\\") || d.endsWith("/")) {
                if (!ROOT_DIR.matches(d)) d = d.trimEnd('\\', '/')
            }
            return d
        }
    }
}
