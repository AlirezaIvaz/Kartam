package ir.alirezaivaz.kartam.extensions

import java.util.Locale

fun Int.formattedMonth(): String {
    return "%02d".format(Locale.ENGLISH, this)
}

fun Int.formattedYear(): String {
    return this.toString().takeLast(2)
}

fun Int.formattedYear(showFull: Boolean): String {
    return if (showFull) {
        "14%02d".format(Locale.ENGLISH, this)
    } else {
        "%02d".format(Locale.ENGLISH, this)
    }
}

fun String.formattedExpirationDate(
    showFull: Boolean,
    showReverse: Boolean,
    expirationMonth: Int,
    expirationYear: Int
): String {
    return if (showReverse) {
        format(
            Locale.ENGLISH,
            expirationYear.formattedYear(showFull),
            expirationMonth.formattedMonth()
        )
    } else {
        format(
            Locale.ENGLISH,
            expirationMonth.formattedMonth(),
            expirationYear.formattedYear(showFull)
        )
    }
}

fun String.formattedShabaNumber(): String {
    if (this.length < 6) return this

    val parts = mutableListOf<String>()
    parts += "IR"
    parts += this.substring(0, 2) // 2 next numbers

    val middle = this.substring(2, this.length - 2)
    parts += middle.chunked(4) // Middle numbers

    parts += this.takeLast(2) // 2 last numbers

    return parts.joinToString(" ")
}
