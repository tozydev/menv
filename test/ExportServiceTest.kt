package vn.id.tozydev.menv

import kotlin.test.Test
import kotlin.test.assertEquals
import vn.id.tozydev.menv.service.ExportFormat
import vn.id.tozydev.menv.service.ExportService

class ExportServiceTest {

    private fun format(entries: List<Pair<String, String>>, f: ExportFormat): String =
        ExportService.format(entries, f)

    @Test
    fun dotenvPlain() {
        assertEquals("FOO=bar\n", format(listOf("FOO" to "bar"), ExportFormat.DOTENV))
    }

    @Test
    fun dotenvQuotesSpacesAndComments() {
        assertEquals("FOO=\"a b\"\n", format(listOf("FOO" to "a b"), ExportFormat.DOTENV))
        assertEquals("FOO=\"a#b\"\n", format(listOf("FOO" to "a#b"), ExportFormat.DOTENV))
    }

    @Test
    fun dotenvEscapesDoubleQuotes() {
        assertEquals("FOO=\"a\\\"b\"\n", format(listOf("FOO" to "a\"b"), ExportFormat.DOTENV))
    }

    @Test
    fun powershellEscapesSingleQuotes() {
        assertEquals("\$env:FOO = 'bar'\n", format(listOf("FOO" to "bar"), ExportFormat.POWERSHELL))
        assertEquals(
            "\$env:FOO = 'it''s'\n",
            format(listOf("FOO" to "it's"), ExportFormat.POWERSHELL),
        )
    }

    @Test
    fun cmdEscapesMetaCharacters() {
        assertEquals("set FOO=a^^b\n", format(listOf("FOO" to "a^b"), ExportFormat.CMD))
        assertEquals("set FOO=a^&b\n", format(listOf("FOO" to "a&b"), ExportFormat.CMD))
        assertEquals("set FOO=a^|b\n", format(listOf("FOO" to "a|b"), ExportFormat.CMD))
        assertEquals("set FOO=a^<b\n", format(listOf("FOO" to "a<b"), ExportFormat.CMD))
        assertEquals("set FOO=a^>b\n", format(listOf("FOO" to "a>b"), ExportFormat.CMD))
    }

    @Test
    fun preservesGivenOrder() {
        val text = format(listOf("A" to "1", "B" to "2"), ExportFormat.DOTENV)
        assertEquals("A=1\nB=2\n", text)
    }

    @Test
    fun fromIdFallsBackToDotenv() {
        assertEquals(ExportFormat.DOTENV, ExportFormat.fromId("bogus"))
        assertEquals(ExportFormat.POWERSHELL, ExportFormat.fromId("powershell"))
    }
}
