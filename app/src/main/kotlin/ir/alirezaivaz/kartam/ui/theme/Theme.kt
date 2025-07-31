package ir.alirezaivaz.kartam.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import ir.alirezaivaz.kartam.dto.isDarkMode
import ir.alirezaivaz.kartam.dto.isDynamicColorsSupported
import ir.alirezaivaz.kartam.utils.SettingsManager

@Composable
fun KartamTheme(
    content: @Composable () -> Unit
) {
    val theme by SettingsManager.theme.collectAsState()
    val language by SettingsManager.language.collectAsState()
    val isDynamicColors by SettingsManager.isDynamicColors.collectAsState()
    val colorScheme = when {
        isDynamicColors && isDynamicColorsSupported -> {
            val context = LocalContext.current
            if (theme.isDarkMode()) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        }
        theme.isDarkMode() -> darkScheme
        else -> lightScheme
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = language.typography,
        content = content
    )
}
