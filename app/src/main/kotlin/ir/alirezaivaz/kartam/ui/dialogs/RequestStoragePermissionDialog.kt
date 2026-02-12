package ir.alirezaivaz.kartam.ui.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
fun RequestStoragePermissionDialog(
    isPermanentlyDenied: Boolean = false,
    onGrantRequest: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    AlertDialog(
        icon = {
            Icon(
                painterResource(R.drawable.ic_database),
                contentDescription = stringResource(R.string.label_storage_permission)
            )
        },
        title = {
            Text(text = stringResource(R.string.label_storage_permission))
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(
                        if (isPermanentlyDenied) {
                            R.string.message_backup_storage_permission_rationale
                        } else {
                            R.string.message_backup_storage_permission
                        }
                    )
                )
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                modifier = Modifier.handPointerIcon(),
                onClick = {
                    onGrantRequest()
                },
            ) {
                Text(
                    text = stringResource(
                        if (isPermanentlyDenied) {
                            R.string.action_open_settings
                        } else {
                            R.string.action_grant_permission
                        }
                    )
                )
            }
        },
        dismissButton = {
            TextButton(
                modifier = Modifier.handPointerIcon(),
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(
                    text = stringResource(R.string.action_dismiss)
                )
            }
        }
    )
}
