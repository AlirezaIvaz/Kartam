package ir.alirezaivaz.kartam.dto

import android.os.Build
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.tablericons.TablerIcons

enum class Theme(
    val icon: Int,
    @get:StringRes
    val title: Int,
    val nightMode: Int,
    val isAvailable: Boolean = true
) {
    Day(
        icon = TablerIcons.Sun,
        title = R.string.theme_day,
        nightMode = AppCompatDelegate.MODE_NIGHT_NO
    ),
    Night(
        icon = TablerIcons.MoonStars,
        title = R.string.theme_night,
        nightMode = AppCompatDelegate.MODE_NIGHT_YES
    ),
    System(
        icon = TablerIcons.BrightnessHalf,
        title = R.string.theme_system,
        nightMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM,
        isAvailable = isSystemNightModeSupported
    );

    companion object {
        fun find(value: String): Theme = entries.find { it.name == value } ?: System
    }
}

@Composable
fun Theme.isDarkMode(): Boolean {
    return this == Theme.Night || (this == Theme.System && isSystemInDarkTheme())
}

val isDynamicColorsSupported: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
val isSystemNightModeSupported: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
