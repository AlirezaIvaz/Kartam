package ir.alirezaivaz.kartam.utils

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import ir.alirezaivaz.kartam.ui.dialogs.RequestStoragePermissionDialog

class StoragePermissionHandler(
    val request: (onGranted: (needReload: Boolean) -> Unit, onDenied: () -> Unit) -> Unit
)

@Composable
fun rememberStoragePermissionHandler(): StoragePermissionHandler {
    val context = LocalContext.current
    val activity = LocalActivity.current

    var showRationale by remember { mutableStateOf(false) }
    var showPermanentlyDenied by remember { mutableStateOf(false) }

    var pendingOnGranted by remember { mutableStateOf<((needReload: Boolean) -> Unit)?>(null) }
    var pendingOnDenied by remember { mutableStateOf<(() -> Unit)?>(null) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            pendingOnGranted?.invoke(true)
        } else {
            val permanentlyDenied = !Utils.isStoragePermissionRequestRationale(activity)
            if (permanentlyDenied) {
                showPermanentlyDenied = true
            } else {
                pendingOnDenied?.invoke()
            }
        }
        pendingOnGranted = null
        pendingOnDenied = null
    }

    val settingsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        val granted = Utils.isStoragePermissionGranted(context)
        if (granted) {
            pendingOnGranted?.invoke(true)
        } else {
            pendingOnDenied?.invoke()
        }
        pendingOnGranted = null
        pendingOnDenied = null
    }

    fun requestPermission(
        onGranted: (needReload: Boolean) -> Unit,
        onDenied: () -> Unit
    ) {
        when {
            Utils.isStoragePermissionGranted(context) -> {
                onGranted(false)
            }

            Utils.isStoragePermissionRequestRationale(activity) -> {
                pendingOnGranted = onGranted
                pendingOnDenied = onDenied
                showRationale = true
            }

            else -> {
                pendingOnGranted = onGranted
                pendingOnDenied = onDenied
                launcher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }

    if (showRationale) {
        RequestStoragePermissionDialog(
            isPermanentlyDenied = false,
            onDismissRequest = {
                showRationale = false
            },
            onGrantRequest = {
                showRationale = false
                launcher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        )
    }
    if (showPermanentlyDenied) {
        RequestStoragePermissionDialog(
            isPermanentlyDenied = true,
            onDismissRequest = {
                showPermanentlyDenied = false
            },
            onGrantRequest = {
                showPermanentlyDenied = false
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", context.packageName, null)
                )
                settingsLauncher.launch(intent)
            }
        )
    }
    return StoragePermissionHandler(::requestPermission)
}
