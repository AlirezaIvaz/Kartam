package ir.alirezaivaz.kartam.ui.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.extensions.handPointerIcon

@Composable
fun PreventScreenshotsConfirmDialog(
    onConfirmRequest: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    AlertDialog(
        icon = {
            Icon(
                painterResource(R.drawable.ic_exclamation_circle),
                contentDescription = stringResource(R.string.settings_prevent_screenshots)
            )
        },
        title = {
            Text(text = stringResource(R.string.settings_prevent_screenshots))
        },
        text = {
            Text(text = stringResource(R.string.settings_prevent_screenshots_description))
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                modifier = Modifier.handPointerIcon(),
                onClick = onConfirmRequest
            ) {
                Text(text = stringResource(R.string.action_confirm))
            }
        },
        dismissButton = {
            TextButton(
                modifier = Modifier.handPointerIcon(),
                onClick = onDismissRequest
            ) {
                Text(text = stringResource(R.string.action_dismiss))
            }
        }
    )
}
