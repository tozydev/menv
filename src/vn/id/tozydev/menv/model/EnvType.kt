package vn.id.tozydev.menv.model

enum class EnvType(val regValue: UInt, val display: String) {
    SZ(1u, "REG_SZ"),
    EXPAND_SZ(2u, "REG_EXPAND_SZ"),
    DWORD(4u, "REG_DWORD"),
    MULTI_SZ(7u, "REG_MULTI_SZ"),
    OTHER(0u, "REG_OTHER");

    companion object {
        fun fromReg(value: UInt): EnvType =
            when (value) {
                1u -> SZ
                2u -> EXPAND_SZ
                4u -> DWORD
                7u -> MULTI_SZ
                else -> OTHER
            }
    }
}
