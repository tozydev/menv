package vn.id.tozydev.menv.model

data class EnvVar(val name: String, val value: String, val type: EnvType)

val ENV_NAME_PATTERN = Regex("^[^=%\u0000\\s]+$")

private val EXPAND_PATTERN = Regex("%[^%]+%")

fun inferType(value: String): EnvType =
    if (EXPAND_PATTERN.containsMatchIn(value)) EnvType.EXPAND_SZ else EnvType.SZ
