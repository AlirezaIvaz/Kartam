package ir.alirezaivaz.kartam.ui.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ir.alirezaivaz.kartam.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteCardDialog(
    snapshot: ImageBitmap?,
    onDeleteRequest: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    AlertDialog(
        icon = {
            Icon(
                painterResource(R.drawable.ic_trash),
                contentDescription = stringResource(R.string.delete_title)
            )
        },
        title = {
            Text(text = stringResource(R.string.delete_title))
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                snapshot?.let {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            bitmap = snapshot,
                            contentDescription = null,
                        )
                    }
                    Spacer(Modifier.height(dimensionResource(R.dimen.padding_vertical)))
                }
                Text(text = stringResource(R.string.delete_message))
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDeleteRequest()
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(text = stringResource(R.string.action_confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(text = stringResource(R.string.action_dismiss))
            }
        }
    )
}
