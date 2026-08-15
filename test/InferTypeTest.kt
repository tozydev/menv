package vn.id.tozydev.menv

import kotlin.test.Test
import kotlin.test.assertEquals
import vn.id.tozydev.menv.model.EnvType
import vn.id.tozydev.menv.model.inferType

class InferTypeTest {

    @Test
    fun infersExpandSzForVariablePatterns() {
        assertEquals(EnvType.EXPAND_SZ, inferType("C:\\Users\\%USERNAME%\\bin"))
        assertEquals(EnvType.EXPAND_SZ, inferType("%SystemRoot%\\system32"))
    }

    @Test
    fun infersPlainSz() {
        assertEquals(EnvType.SZ, inferType("C:\\plain\\path"))
        assertEquals(EnvType.SZ, inferType(""))
        assertEquals(EnvType.SZ, inferType("100%"))
    }
}
