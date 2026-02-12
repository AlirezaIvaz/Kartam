package ir.alirezaivaz.kartam.ui.screens

import android.annotation.SuppressLint
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dokar.sonner.ToasterState
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.dto.Language
import ir.alirezaivaz.kartam.extensions.rememberBackupPicker
import ir.alirezaivaz.kartam.ui.sheets.RestoreBackupSheet
import ir.alirezaivaz.kartam.ui.viewmodel.ManageDataViewModel
import ir.alirezaivaz.kartam.ui.widgets.KartamTopBar
import ir.alirezaivaz.kartam.ui.widgets.backup.BackupFolderPath
import ir.alirezaivaz.kartam.ui.widgets.backup.BackupItem
import ir.alirezaivaz.kartam.ui.widgets.backup.BackupItemCard
import ir.alirezaivaz.kartam.utils.BackupManager
import ir.alirezaivaz.kartam.utils.SettingsManager
import ir.alirezaivaz.kartam.utils.Utils
import ir.alirezaivaz.kartam.utils.rememberStoragePermissionHandler
import ir.mehrafzoon.composedatepicker.dto.toPrettyFormatted
import kotlinx.coroutines.launch

@SuppressLint("LocalContextGetResourceValueCall")
@Composable
fun ManageDataScreen(
    toaster: ToasterState,
    modifier: Modifier = Modifier,
    viewModel: ManageDataViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    val restoreState by viewModel.restoreState.collectAsState()
    val language by SettingsManager.language.collectAsState()
    val isJalali = language == Language.Persian
    val permissionHandler = rememberStoragePermissionHandler()
    val backupPicker = rememberBackupPicker { uri ->
        viewModel.onBackupPicked(uri)
    }

    LaunchedEffect(Unit) {
        viewModel.toastMessage.collect {
            toaster.show(
                type = it.type,
                message = context.getString(it.message)
            )
        }
    }

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            KartamTopBar(
                title = stringResource(R.string.label_manage_data),
            )
        },
    ) {
        if (restoreState.isVisible) {
            RestoreBackupSheet(
                state = restoreState,
                isRestoring = restoreState.isRestoring,
                onConfirm = {
                    viewModel.restoreBackup()
                },
                onDismiss = {
                    viewModel.dismissPreview()
                }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(it)
                .padding(
                    top = dimensionResource(R.dimen.padding_vertical),
                    bottom = 80.dp
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .animateContentSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.label_current_data),
                    style = MaterialTheme.typography.titleLarge
                )
                Crossfade(
                    targetState = state.isLoading
                ) { isLoading ->
                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        BackupItemCard(
                            ownedCount = state.ownedCount,
                            othersCount = state.othersCount,
                            backupTime = state.lastBackupDate?.toPrettyFormatted(isJalali)
                                ?.let { time ->
                                    stringResource(
                                        R.string.label_last_backup_time,
                                        time
                                    )
                                }
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_spacing))
            ) {
                Button(
                    enabled = !state.isLoading,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        permissionHandler.request(
                            { needReload ->
                                scope.launch {
                                    if (needReload) {
                                        viewModel.loadBackups()
                                    }
                                    viewModel.createBackup()
                                }
                            },
                            {
                                viewModel.emitToast(message = R.string.error_permission_denied)
                            }
                        )
                    }
                ) {
                    Text(
                        text = stringResource(R.string.action_new_backup)
                    )
                }
                Button(
                    enabled = !state.isLoading,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.filledTonalButtonColors(),
                    onClick = {
                        scope.launch {
                            backupPicker.launch(arrayOf("*/*"))
                        }
                    }
                ) {
                    Text(
                        text = stringResource(R.string.action_restore_backup)
                    )
                }
            }
            Text(
                text = stringResource(R.string.label_backup_history),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .animateContentSize()
                        .padding(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BackupFolderPath(
                        path = BackupManager.getBackupFolderPath()
                    )
                    Crossfade(
                        targetState = state.isLoading
                    ) { isLoading ->
                        if (isLoading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        } else if (!Utils.isStoragePermissionGranted(context)) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = stringResource(R.string.message_backup_storage_permission_list),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth(),
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Button(
                                    onClick = {
                                        permissionHandler.request(
                                            {
                                                scope.launch {
                                                    viewModel.loadBackups()
                                                }
                                            },
                                            {
                                                viewModel.emitToast(message = R.string.error_permission_denied)
                                            }
                                        )
                                    }
                                ) {
                                    Text(
                                        text = stringResource(R.string.action_grant_permission)
                                    )
                                }
                            }
                        } else if (state.backups.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = stringResource(R.string.message_no_backup)
                                )
                            }
                        } else {
                            Column {
                                state.backups.forEachIndexed { index, file ->
                                    BackupItem(
                                        backup = file,
                                        isJalali = isJalali,
                                        onShareClick = {
                                            BackupManager.shareBackup(context, file)
                                        }
                                    )
                                    if (index < state.backups.lastIndex) {
                                        HorizontalDivider(
                                            modifier = Modifier.padding(horizontal = 16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
