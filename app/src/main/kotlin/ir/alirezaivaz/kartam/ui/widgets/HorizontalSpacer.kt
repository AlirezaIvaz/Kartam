package ir.alirezaivaz.kartam.ui.widgets

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun HorizontalSpacer(
    width: Dp,
    modifier: Modifier = Modifier
) {
    Spacer(modifier = modifier.width(width))
}
