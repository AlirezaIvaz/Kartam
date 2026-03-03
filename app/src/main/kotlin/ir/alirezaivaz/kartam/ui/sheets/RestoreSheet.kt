package ir.alirezaivaz.kartam.ui.sheets

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.dto.Language
import ir.alirezaivaz.kartam.dto.RestoreUiState
import ir.alirezaivaz.kartam.ui.theme.Dimens
import ir.alirezaivaz.kartam.ui.widgets.VerticalSpacer
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
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val language by SettingsManager.language.collectAsState()
    val isJalali = language == Language.Persian

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
                .padding(bottom = Dimens.extraLarge),
            verticalArrangement = Arrangement.spacedBy(Dimens.small)
        ) {
            Text(
                text = stringResource(R.string.label_restore_backup),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.large)
            )
            VerticalSpacer(height = Dimens.large)
            if (state.backup == null || isRestoring) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Dimens.large)
                )
            }
            state.backup?.let {
                BackupItemCard(
                    ownedCount = state.ownedCount,
                    othersCount = state.othersCount,
                    backupTime = it.timestamp.toMixedCalendar().toPrettyFormatted(isJalali),
                    modifier = Modifier.padding(horizontal = Dimens.large)
                )
                VerticalSpacer(height = Dimens.large)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.large),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimens.small)
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
