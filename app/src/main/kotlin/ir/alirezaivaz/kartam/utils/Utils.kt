package ir.alirezaivaz.kartam.utils

import android.content.Context
import android.os.Build
import java.util.Locale

object Utils {

    fun getCardNumber(number: String?): String {
        val sanitized = number.orEmpty().take(16)
        val padded = sanitized.padEnd(16, '-')
        return padded.chunked(4).joinToString(" ")
    }

    fun getCvv2(cvv2: String?, isVisible: Boolean): String {
        if (cvv2 != null) {
            if (isVisible) {
                return cvv2.takeIf { it.length in 3..4 } ?: cvv2.padStart(3, '0')
            }
            val length = cvv2.length.takeIf { it in 3..4 } ?: 3
            return getAsteriskString(length)
        }
        return "".padStart(3, '0')
    }

    fun getAsteriskString(length: Int): String {
        return "*".repeat(length)
    }

    fun getDeviceLanguage(context: Context): String {
        val locale = getSystemLocale(context)
        return normalizeLanguageTag(locale)
    }

    private fun getSystemLocale(context: Context): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0]
        } else {
            @Suppress("DEPRECATION")
            context.resources.configuration.locale
        }
    }

    private fun normalizeLanguageTag(locale: Locale): String {
        val tag = locale.toLanguageTag()
        if (tag.split("-").size > 2) {
            return Locale.Builder()
                .setLanguage(locale.language)
                .setRegion(locale.country)
                .build()
                .toLanguageTag()
        }
        return tag
    }
}
