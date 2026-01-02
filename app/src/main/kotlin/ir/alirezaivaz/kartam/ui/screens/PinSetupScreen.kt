package ir.alirezaivaz.kartam.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.dokar.sonner.rememberToasterState
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.dto.PinStep
import ir.alirezaivaz.kartam.ui.theme.KartamTheme
import ir.alirezaivaz.kartam.ui.widgets.pin.PinScaffold
import ir.alirezaivaz.kartam.utils.SettingsManager

@Composable
fun PinSetupScreen(
    onFinished: () -> Unit
) {
    val toaster = rememberToasterState()
    var step by remember { mutableStateOf(PinStep.Create) }
    var firstPin by remember { mutableStateOf("") }
    var currentPin by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }

    fun onPinCompleted(pin: String) {
        when (step) {
            PinStep.Create -> {
                firstPin = pin
                currentPin = ""
                step = PinStep.Confirm
            }

            PinStep.Confirm -> {
                if (pin == firstPin) {
                    SettingsManager.setPin(pin)
                    onFinished()
                } else {
                    error = true
                    currentPin = ""
                }
            }
        }
    }

    KartamTheme {
        PinScaffold(
            title = if (step == PinStep.Create) {
                stringResource(R.string.action_create_pin)
            } else {
                stringResource(R.string.action_confirm_pin)
            },
            pin = currentPin,
            error = error,
            toaster = toaster,
            biometricEnabled = false,
            onDigit = {
                if (currentPin.length < 6) {
                    currentPin += it
                    error = false
                    if (currentPin.length == 6) {
                        onPinCompleted(currentPin)
                    }
                }
            },
            onRemove = {
                currentPin = currentPin.dropLast(1)
            }
        )
    }
}
