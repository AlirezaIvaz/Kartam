package ir.alirezaivaz.kartam.utils

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.dto.AuthType

class BiometricHelper(
    private val activity: FragmentActivity,
    private val authType: AuthType,
    private val onResult: (Boolean) -> Unit
) {
    private val executor = ContextCompat.getMainExecutor(activity)

    private val biometricPrompt = BiometricPrompt(
        activity,
        executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onResult(true)
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                onResult(false)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                onResult(false)
            }
        }
    )

    fun authenticate() {
        if (authType == AuthType.None) {
            onResult(true)
            return
        }
        if (!isBiometricAvailable(activity, authType)) {
            onResult(false)
            return
        }
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(activity.getString(R.string.message_auth))
            .setSubtitle(activity.getString(R.string.message_auth_description))
            .setConfirmationRequired(true)
            .setAllowedAuthenticators(authType.allowed!!)
            .build()
        biometricPrompt.authenticate(promptInfo)
    }

    companion object {
        fun isBiometricAvailable(context: Context, authType: AuthType): Boolean {
            if (authType.allowed != null) {
                return isAuthenticateAvailable(context, authType.allowed)
            }
            return false
        }

        fun isAuthenticateAvailable(context: Context, authenticator: Int): Boolean {
            val biometricManager = BiometricManager.from(context)
            return biometricManager.canAuthenticate(authenticator) == BiometricManager.BIOMETRIC_SUCCESS
        }
    }
}
