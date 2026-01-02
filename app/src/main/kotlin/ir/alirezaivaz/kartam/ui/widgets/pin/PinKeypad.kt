package ir.alirezaivaz.kartam.ui.widgets.pin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import ir.alirezaivaz.kartam.ui.widgets.DirectionLayout

private val keypadKeys = listOf(
    "1", "2", "3",
    "4", "5", "6",
    "7", "8", "9",
    "fp", "0", "dl"
)

@Composable
fun PinKeypad(
    biometricEnabled: Boolean,
    onDigit: (String) -> Unit,
    onRemove: () -> Unit,
    onBiometric: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    DirectionLayout(layoutDirection = LayoutDirection.Ltr) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            keypadKeys.chunked(3).forEach { row ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    row.forEach { key ->
                        PinKey(
                            label = key,
                            isEnabled = key != "fp" || biometricEnabled,
                            onClick = {
                                when (key) {
                                    "dl" -> onRemove()
                                    "fp" -> onBiometric?.invoke()
                                    else -> onDigit(key)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
