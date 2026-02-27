package ir.alirezaivaz.kartam.ui.widgets.pin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import ir.alirezaivaz.kartam.App
import ir.alirezaivaz.kartam.ui.theme.Dimens
import ir.alirezaivaz.kartam.ui.widgets.DirectionLayout

@Composable
fun PinDots(
    pinLength: Int,
    isError: Boolean,
    modifier: Modifier = Modifier
) {
    DirectionLayout(layoutDirection = LayoutDirection.Ltr) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(Dimens.medium)
        ) {
            repeat(App.DEFAULT_PIN_LENGTH) { index ->
                PinDot(
                    isActive = index < pinLength,
                    isError = isError
                )
            }
        }
    }
}
