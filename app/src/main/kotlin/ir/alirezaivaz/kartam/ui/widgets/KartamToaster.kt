package ir.alirezaivaz.kartam.ui.widgets

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.dokar.sonner.LocalToastContentColor
import com.dokar.sonner.Toaster
import com.dokar.sonner.ToasterState

@Composable
fun KartamToaster(
    state: ToasterState
) {
    Toaster(
        state = state,
        richColors = true,
        darkTheme = isSystemInDarkTheme(),
        alignment = Alignment.TopCenter,
        messageSlot = { toast ->
            val contentColor = LocalToastContentColor.current
            Text(
                text = toast.message.toString(),
                color = contentColor,
                style = MaterialTheme.typography.labelLarge
            )
        }
    )
}
