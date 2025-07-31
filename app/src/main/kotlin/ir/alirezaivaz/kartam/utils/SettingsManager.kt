package ir.alirezaivaz.kartam.utils

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import ir.alirezaivaz.kartam.dto.Language
import ir.alirezaivaz.kartam.dto.Theme
import ir.alirezaivaz.kartam.dto.isDynamicColorsSupported
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object SettingsManager {

    private val settings = Settings()

    private const val PREF_THEME = "pref_theme"
    private const val PREF_LANGUAGE = "pref_language"
    private const val PREF_DYNAMIC_COLORS = "pref_dynamic_colors"

    private val _isDynamicColors = MutableStateFlow(settings[PREF_DYNAMIC_COLORS, isDynamicColorsSupported])
    val isDynamicColors: StateFlow<Boolean> = _isDynamicColors
    private val _theme = MutableStateFlow(Theme.find(settings[PREF_THEME, Theme.System.toString()]))
    val theme: StateFlow<Theme> = _theme
    private val _language = MutableStateFlow(Language.find(settings[PREF_LANGUAGE, Language.English.tag]))
    val language: StateFlow<Language> = _language

    fun isDarkMode(isSystemInDarkTheme: Boolean): Boolean {
        return _theme.value == Theme.Night || (_theme.value == Theme.System && isSystemInDarkTheme)
    }

    fun setTheme(value: Theme) {
        _theme.value = value
        settings[PREF_THEME] = value.toString()
        AppCompatDelegate.setDefaultNightMode(value.nightMode)
    }

    fun setLanguage(value: Language) {
        _language.value = value
        settings[PREF_LANGUAGE] = value.tag
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(value.tag))
    }

    fun setDynamicColors(value: Boolean) {
        _isDynamicColors.value = value
        settings[PREF_DYNAMIC_COLORS] = value
    }

}
