package ir.alirezaivaz.kartam.utils

import android.content.ClipboardManager
import android.content.Context
import ir.alirezaivaz.kartam.dto.DetectedCardData
import ir.alirezaivaz.kartam.extensions.isValidCardNumber
import ir.alirezaivaz.kartam.extensions.isValidName
import ir.alirezaivaz.kartam.extensions.isValidShabaNumber
import ir.alirezaivaz.kartam.extensions.toEnglishDigits

object ClipboardParser {
    private val nameKeywords = listOf("به نام", "بنام", "به‌نام")

    fun getClipboardText(context: Context): String? {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        return clipboard.primaryClip
            ?.getItemAt(0)
            ?.coerceToText(context)
            ?.toString()
            ?.trim()
            ?.takeIf { it.isNotBlank() }
    }

    fun parse(input: String): DetectedCardData? {
        val normalized = input.toEnglishDigits()

        val (card, afterCard) = extractCardNumber(normalized)
        if (card == null) return null

        val (shaba, afterShaba) = extractShabaNumber(afterCard)

        val name = extractName(afterShaba)

        return DetectedCardData(
            name = name,
            cardNumber = card,
            shabaNumber = shaba
        )
    }

    private fun extractCardNumber(text: String): Pair<String?, String> {
        val regex = """\b\d{16}\b|\b\d{4}[- ]\d{4}[- ]\d{4}[- ]\d{4}\b""".toRegex()

        val match = regex.find(text) ?: return null to text

        val raw = match.value.replace(" ", "").replace("-", "")
        val cleaned = raw.takeIf { it.isValidCardNumber() }

        val remaining = text.replace(match.value, "").trim()

        return cleaned to remaining
    }

    private fun extractShabaNumber(text: String): Pair<String?, String> {
        val regex = """IR\d{24}""".toRegex(RegexOption.IGNORE_CASE)

        val match = regex.find(text) ?: return null to text

        val value = match.value.uppercase()
            .takeIf { it.isValidShabaNumber() }

        val remaining = text.replace(match.value, "").trim()

        return value to remaining
    }

    private fun cleanNameText(text: String): String {
        var result = text.substringBefore('\n')
        nameKeywords.forEach {
            result = result.replace(it, "")
        }
        return result.trim()
    }

    private fun extractName(text: String): String? {
        val cleaned = cleanNameText(text)

        return cleaned.takeIf {
            it.isNotBlank() && it.isValidName()
        }
    }
}
