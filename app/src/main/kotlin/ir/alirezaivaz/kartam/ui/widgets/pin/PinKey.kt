package ir.alirezaivaz.kartam.ui.widgets.pin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.extensions.handPointerIcon

@Composable
fun PinKey(
    label: String,
    isEnabled: Boolean = true,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .alpha(1f.takeIf { isEnabled } ?: 0.5f)
            .handPointerIcon(enabled = isEnabled)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable(
                enabled = isEnabled,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        when {
            label.isDigitsOnly() -> {
                Text(label, style = MaterialTheme.typography.titleLarge)
            }

            label == "fp" -> {
                Icon(
                    painter = painterResource(R.drawable.ic_fingerprint),
                    contentDescription = stringResource(R.string.action_scan_biometric)
                )
            }

            label == "dl" -> {
                Icon(
                    painter = painterResource(R.drawable.ic_backspace),
                    contentDescription = stringResource(R.string.action_remove)
                )
            }
        }
    }
}
