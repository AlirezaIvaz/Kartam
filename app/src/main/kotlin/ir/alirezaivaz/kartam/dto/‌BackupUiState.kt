package ir.alirezaivaz.kartam.dto

import ir.mehrafzoon.composedatepicker.dto.MixedCalendar

data class BackupUiState(
    val isLoading: Boolean = false,
    val backups: List<BackupFile> = emptyList(),
    val lastBackupDate: MixedCalendar? = null,
    val ownedCount: Int = 0,
    val othersCount: Int = 0,
)
