package ir.alirezaivaz.kartam.extensions

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon

fun Modifier.handPointerIcon(enabled: Boolean = true): Modifier {
    return this.pointerHoverIcon(PointerIcon.Hand).takeIf { enabled } ?: this
}
