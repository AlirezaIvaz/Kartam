package ir.alirezaivaz.kartam.extensions

import androidx.core.text.isDigitsOnly
import ir.huri.jcal.JalaliCalendar
import java.math.BigInteger

fun String.isValidName(): Boolean {
    val trimmed = this.trim()
    val regex = Regex("^[آ-یA-Za-zء‌\\s]{3,50}$")
    return trimmed.matches(regex)
}

fun String.isValidCardNumber(): Boolean {
    // Check length
    if (this.length != 16 || !this.isDigitsOnly()) return false

    // Apply Luhn algorithm
    val digits = this.map { it.toString().toInt() }
    val checksum = digits
        .mapIndexed { index, digit ->
            if (index % 2 == 0) {
                val doubled = digit * 2
                if (doubled > 9) doubled - 9 else doubled
            } else {
                digit
            }
        }.sum()

    return checksum % 10 == 0
}

fun String.isValidShabaNumber(): Boolean {
    // Check length: must be exactly 24 digits
    if (this.length != 24) return false

    // Add "IR" as per IBAN rules (move to end and convert letters to numbers)
    val fullSheba = "IR$this"

    // Move first 4 characters to the end
    val rearranged = fullSheba.substring(4) + fullSheba.substring(0, 4)

    // Convert letters to numbers: A=10, B=11, ..., Z=35
    val numeric = rearranged.map {
        when (it) {
            in '0'..'9' -> it.toString()
            in 'A'..'Z' -> (it.code - 'A'.code + 10).toString()
            else -> return false
        }
    }.joinToString("")

    // Apply MOD-97
    return numeric.toBigInteger() % BigInteger.valueOf(97) == BigInteger.ONE
}

fun String.isValidAccountNumber(): Boolean {
    return isDigitsOnly() && this.length in 5..24
}

fun String.isValidCvv2(): Boolean {
    return isDigitsOnly() && this.length in 3..4
}

fun String.isValidFirstCode(): Boolean {
    return isDigitsOnly() && length == 4
}

fun String.isValidMonth(): Boolean {
    val month = this.toIntOrNull()
    if (month != null) {
        return month in 1 .. 12
    }
    return false
}

fun String.isValidYear(): Boolean {
    val year = this.toIntOrNull()
    if (year != null) {
        val jalaliCalendar = JalaliCalendar()
        val currentYear = jalaliCalendar.year.toString().takeLast(2).toIntOrNull() ?: 0
        return year >= currentYear
    }
    return false
}
