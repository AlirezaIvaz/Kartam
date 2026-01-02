package ir.alirezaivaz.kartam.dto

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.biometric.BiometricManager.Authenticators
import ir.alirezaivaz.kartam.R

enum class AuthType(
    @param:DrawableRes val icon: Int,
    @param:StringRes val title: Int,
    val allowed: Int?,
    val isBiometric: Boolean = false
) {
    None(
        icon = R.drawable.ic_lock_open,
        title = R.string.auth_none,
        allowed = null
    ),
    Credential(
        icon = R.drawable.ic_password,
        title = R.string.auth_credentials,
        allowed = Authenticators.DEVICE_CREDENTIAL
    ),
    Weak(
        icon = R.drawable.ic_user_scan,
        title = R.string.auth_weak,
        allowed = Authenticators.BIOMETRIC_WEAK or Authenticators.DEVICE_CREDENTIAL,
        isBiometric = true
    ),
    Strong(
        icon = R.drawable.ic_fingerprint_scan,
        title = R.string.auth_strong,
        allowed = Authenticators.BIOMETRIC_STRONG or Authenticators.DEVICE_CREDENTIAL,
        isBiometric = true
    );

    companion object {
        fun find(name: String): AuthType = AuthType.entries.find { it.name == name } ?: None
        fun biometricsOnly(): List<AuthType> = AuthType.entries.filter { it.isBiometric }
    }
}
