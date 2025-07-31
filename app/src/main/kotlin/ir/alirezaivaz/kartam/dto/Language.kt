package ir.alirezaivaz.kartam.dto

import androidx.annotation.StringRes
import androidx.compose.material3.Typography
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.ui.theme.aradTypography
import ir.alirezaivaz.kartam.ui.theme.montserratTypography

enum class Language(
    val tag: String,
    @get:StringRes
    val title: Int,
    val typography: Typography
) {
    English(
        tag = "en-US",
        title = R.string.language_en,
        typography = montserratTypography
    ),
    Persian(
        tag = "fa-IR",
        title = R.string.language_fa,
        typography = aradTypography
    );

    companion object {
        fun find(tag: String): Language = entries.find { it.tag == tag } ?: English
    }
}
