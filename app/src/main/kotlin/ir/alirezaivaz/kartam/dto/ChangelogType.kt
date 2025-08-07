package ir.alirezaivaz.kartam.dto

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import ir.alirezaivaz.kartam.R

enum class ChangelogType(
    val tag: String,
    @get:StringRes val labelRes: Int,
    @get:ColorRes val colorRes: Int,
) {
    NEW(
        tag = "new",
        labelRes = R.string.changelog_type_new,
        colorRes = R.color.changelog_type_new
    ),
    FIXED(
        tag = "fixed",
        labelRes = R.string.changelog_type_fixed,
        colorRes = R.color.changelog_type_fixed
    ),
    IMPROVED(
        tag = "improved",
        labelRes = R.string.changelog_type_improved,
        colorRes = R.color.changelog_type_improved
    ),
    INFO(
        tag = "info",
        labelRes = R.string.changelog_type_info,
        colorRes = R.color.changelog_type_info
    );
    companion object {
        fun fromTag(tag: String): ChangelogType? =
            entries.firstOrNull { it.tag.equals(tag, ignoreCase = true) }
    }
}
