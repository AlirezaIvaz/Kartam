package ir.alirezaivaz.kartam.ui.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import ir.alirezaivaz.kartam.dto.DetectedCardData
import ir.alirezaivaz.kartam.utils.ClipboardMemory
import ir.alirezaivaz.kartam.utils.ClipboardParser

@Composable
fun ClipboardDetector(
    isEnabled: Boolean,
    onDetected: (DetectedCardData) -> Unit
) {
    if (!isEnabled) return

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                val text = ClipboardParser.getClipboardText(context)
                if (text == null || !ClipboardMemory.shouldHandle(text)) {
                    return@LifecycleEventObserver
                }
                val parsed = ClipboardParser.parse(text)
                if (parsed?.cardNumber != null || parsed?.shabaNumber != null) {
                    onDetected(parsed)
                }
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}
