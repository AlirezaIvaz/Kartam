package ir.alirezaivaz.kartam.utils

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import ir.alirezaivaz.kartam.BuildConfig
import ir.alirezaivaz.kartam.dto.AuthType
import ir.alirezaivaz.kartam.dto.Language
import ir.alirezaivaz.kartam.dto.Theme
import ir.alirezaivaz.kartam.dto.isDynamicColorsSupported
import ir.alirezaivaz.kartam.extensions.sha256
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object SettingsManager {

    private val settings = Settings()

    private const val PREF_VERSION = "pref_version"
    private const val PREF_THEME = "pref_theme"
    private const val PREF_LANGUAGE = "pref_language"
    private const val PREF_AUTH_TYPE = "pref_auth_type"
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
    private const val PREF_LOCK_ON_START = "pref_lock_on_start"
    private const val PREF_LOCK_ON_START_DEFAULT = false
    private const val PREF_UNLOCK_WITH_BIOMETRIC = "pref_unlock_with_biometric"
    private const val PREF_UNLOCK_WITH_BIOMETRIC_DEFAULT = false
    private const val PREF_PIN_HASH = "pref_pin_hash"
    private const val PREF_AUTH_OWNED_CARD_DETAILS = "pref_auth_owned_card_details"
    private const val PREF_AUTH_OWNED_CARD_DETAILS_DEFAULT = true
    private const val PREF_AUTH_SECRET_DATA = "pref_auth_secret_items"
    private const val PREF_AUTH_SECRET_DATA_DEFAULT = true
    private const val PREF_AUTH_BEFORE_EDIT = "pref_auth_before_edit"
    private const val PREF_AUTH_BEFORE_EDIT_DEFAULT = true
    private const val PREF_AUTH_BEFORE_DELETE = "pref_auth_before_delete"
    private const val PREF_AUTH_BEFORE_DELETE_DEFAULT = true

    private val _version = MutableStateFlow(settings[PREF_VERSION, BuildConfig.VERSION_CODE])
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
    private val _language = MutableStateFlow(Language.find(settings[PREF_LANGUAGE, Language.DEFAULT_LANGUAGE.tag]))
    val language: StateFlow<Language> = _language
    private val _isLockOnStart = MutableStateFlow(settings[PREF_LOCK_ON_START, PREF_LOCK_ON_START_DEFAULT])
    val isLockOnStart: StateFlow<Boolean> = _isLockOnStart
    private val _isUnlockWithBiometric = MutableStateFlow(settings[PREF_UNLOCK_WITH_BIOMETRIC, PREF_UNLOCK_WITH_BIOMETRIC_DEFAULT])
    val isUnlockWithBiometric: StateFlow<Boolean> = _isUnlockWithBiometric
    private val _authType = MutableStateFlow(AuthType.find(settings[PREF_AUTH_TYPE, AuthType.None.name]))
    val authType: StateFlow<AuthType> = _authType
    private val _isAuthOwnedCardDetails = MutableStateFlow(settings[PREF_AUTH_OWNED_CARD_DETAILS, PREF_AUTH_OWNED_CARD_DETAILS_DEFAULT])
    val isAuthOwnedCardDetails: StateFlow<Boolean> = _isAuthOwnedCardDetails
    private val _isAuthSecretData = MutableStateFlow(settings[PREF_AUTH_SECRET_DATA, PREF_AUTH_SECRET_DATA_DEFAULT])
    val isAuthSecretData: StateFlow<Boolean> = _isAuthSecretData
    private val _isAuthBeforeEdit = MutableStateFlow(settings[PREF_AUTH_BEFORE_EDIT, PREF_AUTH_BEFORE_EDIT_DEFAULT])
    val isAuthBeforeEdit: StateFlow<Boolean> = _isAuthBeforeEdit
    private val _isAuthBeforeDelete = MutableStateFlow(settings[PREF_AUTH_BEFORE_DELETE, PREF_AUTH_BEFORE_DELETE_DEFAULT])
    val isAuthBeforeDelete: StateFlow<Boolean> = _isAuthBeforeDelete

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

    fun getLanguage(): String {
        return settings[PREF_LANGUAGE, Language.DEFAULT_LANGUAGE.tag]
    }

    fun setLanguage(value: Language, isFromUser: Boolean = true) {
        _language.value = value
        settings[PREF_LANGUAGE] = value.tag
        if (isFromUser) {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(value.tag))
        }
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

    fun setLockOnStart(value: Boolean) {
        _isLockOnStart.value = value
        settings[PREF_LOCK_ON_START] = value
        if (!value) {
            clearPin()
            setUnlockWithBiometric(false)
        }
    }

    fun setUnlockWithBiometric(value: Boolean) {
        _isUnlockWithBiometric.value = value
        settings[PREF_UNLOCK_WITH_BIOMETRIC] = value
    }

    fun setPin(pin: String) {
        settings[PREF_PIN_HASH] = pin.sha256()
    }

    fun verifyPin(pin: String): Boolean {
        val saved = settings[PREF_PIN_HASH, ""].ifEmpty { return false }
        return pin.sha256() == saved
    }

    fun clearPin() {
        settings.remove(PREF_PIN_HASH)
    }

    fun setAuthType(value: AuthType) {
        _authType.value = value
        settings[PREF_AUTH_TYPE] = value.name
        if (value == AuthType.None) {
            setUnlockWithBiometric(false)
        }
    }

    fun setAuthOwnedCardDetails(value: Boolean) {
        _isAuthOwnedCardDetails.value = value
        settings[PREF_AUTH_OWNED_CARD_DETAILS] = value
    }

    fun setAuthSecretData(value: Boolean) {
        _isAuthSecretData.value = value
        settings[PREF_AUTH_SECRET_DATA] = value
    }

    fun setAuthBeforeEdit(value: Boolean) {
        _isAuthBeforeEdit.value = value
        settings[PREF_AUTH_BEFORE_EDIT] = value
    }

    fun setAuthBeforeDelete(value: Boolean) {
        _isAuthBeforeDelete.value = value
        settings[PREF_AUTH_BEFORE_DELETE] = value
    }

}
