package ir.alirezaivaz.kartam.dto

@JvmInline
value class SensitiveString(val value: String)

fun SensitiveString?.toStringOrNull(): String? {
    if (this?.value.isNullOrBlank()) {
        return null
    }
    return this.value
}

fun String?.toSensitive(): SensitiveString? {
    if (this.isNullOrBlank()) {
        return null
    }
    return SensitiveString(this)
}
