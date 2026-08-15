package vn.id.tozydev.menv

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import vn.id.tozydev.menv.model.ENV_NAME_PATTERN

class EnvNameTest {

    @Test
    fun validNames() {
        assertTrue(ENV_NAME_PATTERN.matches("FOO_BAR"))
        assertTrue(ENV_NAME_PATTERN.matches("foo.bar1"))
        assertTrue(ENV_NAME_PATTERN.matches("PATH"))
    }

    @Test
    fun invalidNames() {
        assertFalse(ENV_NAME_PATTERN.matches(""))
        assertFalse(ENV_NAME_PATTERN.matches("A=B"))
        assertFalse(ENV_NAME_PATTERN.matches("A%B"))
        assertFalse(ENV_NAME_PATTERN.matches("A\u0000B"))
        assertFalse(ENV_NAME_PATTERN.matches("A B"))
    }
}
