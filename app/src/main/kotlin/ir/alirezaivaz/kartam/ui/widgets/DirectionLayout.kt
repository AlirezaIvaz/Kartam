package ir.alirezaivaz.kartam.ui.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

@Composable
fun DirectionLayout(
    layoutDirection: LayoutDirection,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        value = LocalLayoutDirection provides layoutDirection,
        content = content
    )
}
