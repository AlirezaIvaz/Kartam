package ir.alirezaivaz.kartam.dto

data class RestoreUiState(
    val isVisible: Boolean = false,
    val backup: Backup? = null,
    val ownedCount: Int = 0,
    val othersCount: Int = 0,
    val isRestoring: Boolean = false,
)
