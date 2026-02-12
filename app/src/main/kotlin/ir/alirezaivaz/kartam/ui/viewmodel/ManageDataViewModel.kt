package ir.alirezaivaz.kartam.ui.viewmodel

import android.net.Uri
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dokar.sonner.ToastType
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.dto.BackupUiState
import ir.alirezaivaz.kartam.dto.RestoreUiState
import ir.alirezaivaz.kartam.dto.ToastMessage
import ir.alirezaivaz.kartam.utils.BackupManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ManageDataViewModel : ViewModel() {
    private val _state = MutableStateFlow(BackupUiState())
    val state = _state.asStateFlow()
    private val _restoreState = MutableStateFlow(RestoreUiState())
    val restoreState = _restoreState.asStateFlow()
    private val _toastMessage = MutableSharedFlow<ToastMessage>()
    val toastMessage = _toastMessage.asSharedFlow()

    init {
        loadBackups()
    }

    fun loadBackups() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val ownedCount = BackupManager.getOwnedCount()
            val othersCount = BackupManager.getOthersCount()
            val backups = withContext(Dispatchers.IO) {
                BackupManager.listBackups()
            }
            _state.update {
                it.copy(
                    isLoading = false,
                    backups = backups,
                    lastBackupDate = backups.firstOrNull()?.date,
                    ownedCount = ownedCount,
                    othersCount = othersCount,
                )
            }
        }
    }

    fun createBackup() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            withContext(Dispatchers.IO) {
                BackupManager.createManualBackup()
            }
            loadBackups()
        }
    }

    fun onBackupPicked(uri: Uri) {
        viewModelScope.launch {
            runCatching {
                _restoreState.update { it.copy(isVisible = true) }
                val backup = withContext(Dispatchers.IO) {
                    BackupManager.readBackupFromUri(uri)
                }
                _restoreState.update {
                    it.copy(
                        backup = backup,
                        ownedCount = backup.cards.count { it.isOwned },
                        othersCount = backup.cards.count { !it.isOwned },
                    )
                }
            }.onFailure {
                dismissPreview()
                emitToast(message = R.string.error_invalid_backup_file)
            }
        }
    }

    fun restoreBackup() {
        viewModelScope.launch {
            val backup = _restoreState.value.backup
            if (backup == null) {
                emitToast(message = R.string.error_invalid_backup_file)
                return@launch
            }
            _restoreState.update { it.copy(isRestoring = true) }
            runCatching {
                withContext(Dispatchers.IO) {
                    BackupManager.restoreBackup(backup)
                }
            }.onSuccess {
                dismissPreview()
                loadBackups()
                emitToast(
                    type = ToastType.Success,
                    message = R.string.message_backup_restored
                )
            }.onFailure {
                dismissPreview()
                emitToast(message = R.string.error_restore_backup_failed)
            }
        }
    }

    fun dismissPreview() {
        _restoreState.update {
            RestoreUiState()
        }
    }

    fun emitToast(
        @StringRes
        message: Int,
        type: ToastType = ToastType.Error,
    ) {
        viewModelScope.launch {
            _toastMessage.emit(ToastMessage(message, type))
        }
    }
}
