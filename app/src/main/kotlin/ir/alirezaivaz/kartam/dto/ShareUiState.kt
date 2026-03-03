package ir.alirezaivaz.kartam.dto

data class ShareUiState(
    val includeCardNumber: Boolean = true,
    val includeShabaNumber: Boolean = false,
    val includeAccountNumber: Boolean = false,
    val includeOwnerName: Boolean = true,
    val includeBankName: Boolean = false,
    val advancedFormat: Boolean = false,
    val useCardSeparator: Boolean = true,
    val usePersianDigits: Boolean = false,
)
