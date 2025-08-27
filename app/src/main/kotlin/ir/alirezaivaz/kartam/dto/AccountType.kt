package ir.alirezaivaz.kartam.dto

import androidx.annotation.StringRes
import ir.alirezaivaz.kartam.R

enum class AccountType(
    @param:StringRes
    val title: Int,
) {
    ActiveQarzAlHasaneh(title = R.string.account_active_qarz_al_hasaneh),
    SavingQarzAlHasaneh(title = R.string.account_saving_qarz_al_hasaneh),
    ShortTermDeposit(title = R.string.account_short_term_deposit),
    LongTermDeposit(title = R.string.account_long_term_deposit),
    JointAccount(title = R.string.account_joint);
}
