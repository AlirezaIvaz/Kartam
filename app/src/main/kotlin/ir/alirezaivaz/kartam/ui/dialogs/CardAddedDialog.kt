package ir.alirezaivaz.kartam.ui.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.extensions.handPointerIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardAddedDialog(
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    AlertDialog(
        icon = {
            Icon(
                painterResource(R.drawable.ic_circle_check),
                contentDescription = stringResource(R.string.card_added_title)
            )
        },
        title = {
            Text(text = stringResource(R.string.card_added_title))
        },
        text = {
            Text(text = stringResource(R.string.card_added_message))
        },
        onDismissRequest = {},
        confirmButton = {
            TextButton(
                modifier = Modifier.handPointerIcon(),
                onClick = {
                    onConfirm()
                }
            ) {
                Text(text = stringResource(R.string.action_confirm))
            }
        },
        dismissButton = {
            TextButton(
                modifier = Modifier.handPointerIcon(),
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(text = stringResource(R.string.action_stay_here))
            }
        }
    )
}
