package ir.alirezaivaz.kartam.ui.widgets

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
    )
}
