package ir.alirezaivaz.kartam.ui.screens

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.FragmentActivity
import com.dokar.sonner.ToastType
import com.dokar.sonner.rememberToasterState
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.dto.AuthType
import ir.alirezaivaz.kartam.ui.theme.KartamTheme
import ir.alirezaivaz.kartam.ui.widgets.pin.PinScaffold
import ir.alirezaivaz.kartam.utils.BiometricHelper
import ir.alirezaivaz.kartam.utils.SettingsManager

@Composable
fun PinLockScreen(
    onUnlocked: () -> Unit
) {
    val activity = LocalActivity.current
    val resources = LocalResources.current
    val toaster = rememberToasterState()
    var pin by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }
    val authType by SettingsManager.authType.collectAsState()
    val isUnlockWithBiometric by SettingsManager.isUnlockWithBiometric.collectAsState()

    KartamTheme {
        PinScaffold(
            title = stringResource(R.string.action_enter_pin),
            pin = pin,
            error = error,
            toaster = toaster,
            biometricEnabled = isUnlockWithBiometric && authType in AuthType.biometricsOnly(),
            onDigit = {
                if (pin.length < 6) {
                    pin += it
                    error = false
                    if (pin.length == 6) {
                        if (SettingsManager.verifyPin(pin)) {
                            onUnlocked()
                        } else {
                            error = true
                        }
                    }
                }
            },
            onRemove = {
                pin = pin.dropLast(1)
                error = false
            },
            onBiometric = {
                if (isUnlockWithBiometric && authType in AuthType.biometricsOnly()) {
                    BiometricHelper(
                        activity = activity as FragmentActivity,
                        authType = authType,
                        onResult = {
                            if (it) {
                                onUnlocked()
                            } else {
                                toaster.show(
                                    message = resources.getString(R.string.error_authentication_failed),
                                    type = ToastType.Error
                                )
                            }
                        }
                    ).authenticate()
                }
            }
        )
    }
}
