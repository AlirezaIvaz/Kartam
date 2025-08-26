package ir.alirezaivaz.kartam.utils

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import ir.alirezaivaz.kartam.BuildConfig
import ir.alirezaivaz.kartam.dto.Language
import ir.alirezaivaz.kartam.dto.Theme
import ir.alirezaivaz.kartam.dto.isDynamicColorsSupported
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object SettingsManager {

    private val settings = Settings()

    private const val PREF_VERSION = "pref_version"
    private const val PREF_THEME = "pref_theme"
    private const val PREF_LANGUAGE = "pref_language"
    private const val PREF_DYNAMIC_COLORS = "pref_dynamic_colors"
    private const val PREF_SHOW_SHABA_NUMBER = "pref_show_shaba_number"
    private const val PREF_SHOW_SHABA_NUMBER_DEFAULT = true
    private const val PREF_SHOW_FULL_EXP_DATE = "pref_show_full_expiration_date"
    private const val PREF_SHOW_FULL_EXP_DATE_DEFAULT = true
    private const val PREF_SHOW_REVERSE_EXP_DATE = "pref_show_reverse_expiration_date"
    private const val PREF_SHOW_REVERSE_EXP_DATE_DEFAULT = true
    private const val PREF_SECRET_CVV2_LIST = "pref_secret_cvv2_list"
    private const val PREF_SECRET_CVV2_LIST_DEFAULT = true
    private const val PREF_SECRET_CVV2_DETAILS = "pref_secret_cvv2_details"
    private const val PREF_SECRET_CVV2_DETAILS_DEFAULT = false

    private val _version = MutableStateFlow(settings[PREF_VERSION, 0])
    val version: StateFlow<Int> = _version
    private val _isDynamicColors = MutableStateFlow(settings[PREF_DYNAMIC_COLORS, isDynamicColorsSupported])
    val isDynamicColors: StateFlow<Boolean> = _isDynamicColors
    private val _isShowShabaNumberInCard = MutableStateFlow(settings[PREF_SHOW_SHABA_NUMBER, PREF_SHOW_SHABA_NUMBER_DEFAULT])
    val isShowShabaNumberInCard: StateFlow<Boolean> = _isShowShabaNumberInCard
    private val _isShowFullExpirationDate = MutableStateFlow(settings[PREF_SHOW_FULL_EXP_DATE, PREF_SHOW_FULL_EXP_DATE_DEFAULT])
    val isShowFullExpirationDate: StateFlow<Boolean> = _isShowFullExpirationDate
    private val _isShowReverseExpirationDate = MutableStateFlow(settings[PREF_SHOW_REVERSE_EXP_DATE, PREF_SHOW_REVERSE_EXP_DATE_DEFAULT])
    val isShowReverseExpirationDate: StateFlow<Boolean> = _isShowReverseExpirationDate
    private val _isSecretCvv2InList = MutableStateFlow(settings[PREF_SECRET_CVV2_LIST, PREF_SECRET_CVV2_LIST_DEFAULT])
    val isSecretCvv2InList: StateFlow<Boolean> = _isSecretCvv2InList
    private val _isSecretCvv2InDetails = MutableStateFlow(settings[PREF_SECRET_CVV2_DETAILS, PREF_SECRET_CVV2_DETAILS_DEFAULT])
    val isSecretCvv2InDetails: StateFlow<Boolean> = _isSecretCvv2InDetails
    private val _theme = MutableStateFlow(Theme.find(settings[PREF_THEME, Theme.System.toString()]))
    val theme: StateFlow<Theme> = _theme
    private val _language = MutableStateFlow(Language.find(settings[PREF_LANGUAGE, Language.English.tag]))
    val language: StateFlow<Language> = _language

    fun isDarkMode(isSystemInDarkTheme: Boolean): Boolean {
        return _theme.value == Theme.Night || (_theme.value == Theme.System && isSystemInDarkTheme)
    }

    fun isAppUpdated(): Boolean {
        return BuildConfig.VERSION_CODE > _version.value
    }

    fun setLastVersion() {
        val value = BuildConfig.VERSION_CODE
        _version.value = value
        settings[PREF_VERSION] = value
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

    fun setShowShabaNumberInCard(value: Boolean) {
        _isShowShabaNumberInCard.value = value
        settings[PREF_SHOW_SHABA_NUMBER] = value
    }

    fun setShowFullExpirationDate(value: Boolean) {
        _isShowFullExpirationDate.value = value
        settings[PREF_SHOW_FULL_EXP_DATE] = value
    }

    fun setShowReverseExpirationDate(value: Boolean) {
        _isShowReverseExpirationDate.value = value
        settings[PREF_SHOW_REVERSE_EXP_DATE] = value
    }

    fun setSecretCvv2List(value: Boolean) {
        _isSecretCvv2InList.value = value
        settings[PREF_SECRET_CVV2_LIST] = value
    }

    fun setSecretCvv2Details(value: Boolean) {
        _isSecretCvv2InDetails.value = value
        settings[PREF_SECRET_CVV2_DETAILS] = value
    }

}
