package ir.alirezaivaz.kartam.dto

import androidx.annotation.StringRes
import androidx.compose.material3.Typography
import androidx.compose.ui.unit.LayoutDirection
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.ui.theme.aradTypography
import ir.alirezaivaz.kartam.ui.theme.montserratTypography

enum class Language(
    val tag: String,
    @get:StringRes
    val title: Int,
    val direction: LayoutDirection,
    val typography: Typography
) {
    English(
        tag = "en-US",
        title = R.string.language_en,
        direction = LayoutDirection.Ltr,
        typography = montserratTypography
    ),
    Persian(
        tag = "fa-IR",
        title = R.string.language_fa,
        direction = LayoutDirection.Rtl,
        typography = aradTypography
    );

    companion object {
        val DEFAULT_LANGUAGE = Persian
        fun find(tag: String): Language = entries.find { it.tag == tag } ?: DEFAULT_LANGUAGE
    }
}
