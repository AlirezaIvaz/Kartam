package ir.alirezaivaz.kartam.ui.widgets.pin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.dokar.sonner.ToasterState
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.ui.widgets.KartamToaster

@OptIn(ExperimentalMaterial3Api::class)
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
            CenterAlignedTopAppBar(
                title = {
                    Text(text = title)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
            )
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

