package ir.alirezaivaz.kartam.ui.sheets

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.dto.Language
import ir.alirezaivaz.kartam.dto.RestoreUiState
import ir.alirezaivaz.kartam.ui.widgets.backup.BackupItemCard
import ir.alirezaivaz.kartam.utils.SettingsManager
import ir.mehrafzoon.composedatepicker.dto.toMixedCalendar
import ir.mehrafzoon.composedatepicker.dto.toPrettyFormatted

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestoreBackupSheet(
    state: RestoreUiState,
    isRestoring: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val language by SettingsManager.language.collectAsState()
    val isJalali = language == Language.Persian

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
                .padding(bottom = 50.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.label_restore_backup),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(16.dp))
            if (state.backup == null || isRestoring) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                )
            }
            state.backup?.let {
                BackupItemCard(
                    ownedCount = state.ownedCount,
                    othersCount = state.othersCount,
                    backupTime = it.timestamp.toMixedCalendar().toPrettyFormatted(isJalali),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        enabled = !isRestoring,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = stringResource(R.string.action_dismiss)
                        )
                    }
                    Button(
                        onClick = onConfirm,
                        enabled = !isRestoring,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = stringResource(R.string.action_restore)
                        )
                    }
                }
            }
        }
    }
}
