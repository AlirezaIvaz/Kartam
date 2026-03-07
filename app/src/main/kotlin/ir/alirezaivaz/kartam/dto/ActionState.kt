package ir.alirezaivaz.kartam.dto

import androidx.annotation.StringRes
import ir.alirezaivaz.kartam.R

enum class ActionState(
    val isSuccess: Boolean = false,
    @param:StringRes
    val message: Int? = null,
) {
    CardAdded(
        isSuccess = true,
        message = null,
    ),
    CardUpdated(
        isSuccess = true,
        message = R.string.message_card_updated,
    ),
    CardNotFound(
        message = R.string.message_went_wrong_description,
    ),
    EmptyCardNumber(
        message = R.string.error_empty_card_number,
    ),
    EmptyOwnerName(
        message = R.string.error_empty_owner_name,
    ),
    InvalidCardNumber(
        message = R.string.error_invalid_card_number,
    ),
    InvalidOwnerName(
        message = R.string.error_invalid_owner_name,
    ),
    InvalidShabaNumber(
        message = R.string.error_invalid_shaba_number,
    ),
    InvalidAccountNumber(
        message = R.string.error_invalid_account_number,
    ),
    InvalidCVV2(
        message = R.string.error_invalid_cvv2,
    ),
    InvalidFirstCode(
        message = R.string.error_invalid_first_code,
    ),
    InvalidBranchCode(
        message = R.string.error_invalid_branch_code,
    ),
    InvalidBranchName(
        message = R.string.error_invalid_branch_name,
    ),
    InvalidExpirationMonth(
        message = R.string.error_invalid_exp_month,
    ),
    InvalidExpirationYear(
        message = R.string.error_invalid_exp_year,
    ),
}
