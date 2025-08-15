package ir.alirezaivaz.kartam.ui.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.drawToBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun SnapshotableCard(
    modifier: Modifier = Modifier,
    onSnapshotReady: (ImageBitmap) -> Unit,
    content: @Composable () -> Unit
) {
    var composeView: ComposeView? by remember { mutableStateOf(null) }

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            ComposeView(ctx).apply {
                setContent(content = content)
                composeView = this
            }
        }
    )

    LaunchedEffect(composeView) {
        composeView?.let { view ->
            withContext(Dispatchers.Main) {
                val bitmap = view.drawToBitmap()
                onSnapshotReady(bitmap.asImageBitmap())
            }
        }
    }
}
