package ir.alirezaivaz.kartam.ui.widgets.pin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.dokar.sonner.ToasterState
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.ui.widgets.KartamToaster
import ir.alirezaivaz.kartam.ui.widgets.KartamTopBar

@Composable
fun PinScaffold(
    title: String,
    pin: String,
    error: Boolean,
    toaster: ToasterState,
    biometricEnabled: Boolean,
    onDigit: (String) -> Unit,
    onRemove: () -> Unit,
    onBiometric: (() -> Unit)? = null,
) {
    LaunchedEffect(Unit) {
        onBiometric?.invoke()
    }
    Scaffold(
        topBar = {
            KartamTopBar(title = title)
        },
    ) {
        KartamToaster(toaster)
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Spacer(Modifier.weight(1f))
            PinDots(pinLength = pin.length, isError = error)
            Spacer(Modifier.weight(1f))
            PinKeypad(
                biometricEnabled = biometricEnabled,
                onDigit = onDigit,
                onRemove = onRemove,
                onBiometric = onBiometric
            )
            Spacer(Modifier.height(dimensionResource(R.dimen.padding_vertical)))
        }
    }
}

