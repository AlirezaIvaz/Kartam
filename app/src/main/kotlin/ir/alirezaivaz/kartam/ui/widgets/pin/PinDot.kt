package ir.alirezaivaz.kartam.ui.widgets.pin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PinDot(
    isActive: Boolean,
    isError: Boolean,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when {
        isActive && isError -> MaterialTheme.colorScheme.error
        isActive && !isError -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    Box(
        modifier = modifier
            .size(14.dp)
            .background(
                color = backgroundColor,
                shape = CircleShape
            )
    )
}
