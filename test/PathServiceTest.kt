package vn.id.tozydev.menv

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import vn.id.tozydev.menv.model.PathList

class PathServiceTest {

    @Test
    fun addToEmptyRawPath() {
        val r = PathList("").add("C:\\bin", prepend = false)
        assertTrue(r.changed)
        assertEquals("C:\\bin", r.raw)
    }

    @Test
    fun addEmptyDirIsNoop() {
        val r = PathList("C:\\a").add("   ", prepend = false)
        assertFalse(r.changed)
        assertEquals("C:\\a", r.raw)
        assertEquals("", r.normalized)
    }

    @Test
    fun addKeepsDriveRootWithSlash() {
        val r = PathList("").add("D:\\", prepend = false)
        assertEquals("D:\\", r.normalized)
    }

    @Test
    fun removeNormalizesTrailingSlash() {
        val r = PathList("C:\\a;C:\\b").remove("C:\\b\\")
        assertTrue(r.changed)
        assertEquals("C:\\a", r.raw)
    }

    @Test
    fun removeLeavesExpandEntriesIntact() {
        val r = PathList("%USERPROFILE%\\bin;C:\\a").remove("C:\\a")
        assertTrue(r.changed)
        assertEquals("%USERPROFILE%\\bin", r.raw)
    }

    @Test
    fun removeCaseInsensitiveAcrossDrives() {
        val r = PathList("C:\\A;c:\\B").remove("c:\\b")
        assertTrue(r.changed)
        assertEquals("C:\\A", r.raw)
    }
}
