package ir.alirezaivaz.kartam.extensions

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

@Composable
fun rememberBackupPicker(
    onPicked: (Uri) -> Unit
): ActivityResultLauncher<Array<String>> {
    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let(onPicked)
        }
    )
}
