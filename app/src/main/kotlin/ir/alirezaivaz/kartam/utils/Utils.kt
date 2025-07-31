package ir.alirezaivaz.kartam.utils

object Utils {

    fun getCardNumber(number: String?): String {
        val sanitized = number.orEmpty().take(16)
        val padded = sanitized.padEnd(16, '-')
        return padded.chunked(4).joinToString(" ")
    }

    fun getCvv2(cvv2: String?, isVisible: Boolean): String {
        if (isVisible && cvv2 != null) {
            return cvv2.takeIf { it.length in 3..4 } ?: cvv2.padStart(3, '0')
        }
        val length = cvv2?.length.takeIf { it in 3..4 } ?: 3
        return getAsteriskString(length)
    }

    fun getAsteriskString(length: Int): String {
        return "*".repeat(length)
    }

}
