package ir.alirezaivaz.kartam.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ir.alirezaivaz.kartam.utils.BackupManager
import ir.alirezaivaz.kartam.utils.KartamDatabase

class AddCardViewModelFactory(
    private val db: KartamDatabase,
    private val cardId: Int,
    private val isOwned: Boolean,
    private val backupManager: BackupManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddCardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddCardViewModel(db, cardId, isOwned, backupManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
