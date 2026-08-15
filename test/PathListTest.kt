package vn.id.tozydev.menv

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import vn.id.tozydev.menv.model.PathList

class PathListTest {

    @Test
    fun parseSplitsAndFilters() {
        assertEquals(listOf("a", "b"), PathList("a;b;").entries)
        assertEquals(listOf("a", "b"), PathList(" a ; b ").entries)
        assertEquals(emptyList(), PathList(";;").entries)
        assertEquals(emptyList(), PathList("").entries)
    }

    @Test
    fun addDedupesCaseInsensitive() {
        val r = PathList("C:\\A;C:\\b").add("c:\\a", prepend = false)
        assertFalse(r.changed)
        assertEquals("C:\\A;C:\\b", r.raw)
    }

    @Test
    fun addAppendsAndPrepends() {
        val appended = PathList("C:\\a").add("C:\\b", prepend = false)
        assertTrue(appended.changed)
        assertEquals("C:\\a;C:\\b", appended.raw)

        val prepended = PathList("C:\\a").add("C:\\b", prepend = true)
        assertTrue(prepended.changed)
        assertEquals("C:\\b;C:\\a", prepended.raw)
    }

    @Test
    fun addNormalizesTrailingSeparators() {
        val r = PathList("").add("C:\\a\\", prepend = false)
        assertTrue(r.changed)
        assertEquals("C:\\a", r.normalized)
        assertEquals("C:\\a", r.raw)
    }

    @Test
    fun addKeepsDriveRoot() {
        val r = PathList("").add("C:\\", prepend = false)
        assertEquals("C:\\", r.normalized)
    }

    @Test
    fun removeRemovesAllMatches() {
        val r = PathList("C:\\A;C:\\b;C:\\a").remove("c:\\a")
        assertTrue(r.changed)
        assertEquals("C:\\b", r.raw)
    }

    @Test
    fun removeMissingIsNoop() {
        val r = PathList("C:\\a").remove("C:\\zz")
        assertFalse(r.changed)
        assertEquals("C:\\a", r.raw)
    }
}
