package ir.alirezaivaz.kartam.dto

import androidx.annotation.StringRes
import ir.alirezaivaz.kartam.R

data class Result(
    val isSuccess: Boolean = false,
    @param:StringRes
    val message: Int? = null,
    val errorCode: ErrorCode? = null,
    val data: List<Any> = emptyList()
)

enum class ErrorCode(
    @get:StringRes
    val message: Int
) {
    WentWrong(
        message = R.string.message_went_wrong_description
    ),
    EmptyCardNumber(
        message = R.string.error_empty_card_number
    ),
    EmptyCardOwnerName(
        message = R.string.error_empty_owner_name
    ),
    InvalidCardNumber(
        message = R.string.error_invalid_card_number
    ),
    InvalidCardOwnerName(
        message = R.string.error_invalid_owner_name
    ),
    InvalidShabaNumber(
        message = R.string.error_invalid_shaba_number
    ),
    InvalidAccountNumber(
        message = R.string.error_invalid_account_number
    ),
    InvalidCvv2(
        message = R.string.error_invalid_cvv2
    ),
    InvalidExpirationMonth(
        message = R.string.error_invalid_exp_month
    ),
    InvalidExpirationYear(
        message = R.string.error_invalid_exp_year
    )
}
