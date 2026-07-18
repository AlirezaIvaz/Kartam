package ir.alirezaivaz.kartam.dto

import androidx.annotation.StringRes
import com.google.gson.annotations.SerializedName
import ir.alirezaivaz.kartam.R

enum class AccountType(
    @param:StringRes
    val title: Int,
) {
    @SerializedName("ActiveQarzAlHasaneh")
    ActiveQarzAlHasaneh(title = R.string.account_active_qarz_al_hasaneh),
    @SerializedName("SavingQarzAlHasaneh")
    SavingQarzAlHasaneh(title = R.string.account_saving_qarz_al_hasaneh),
    @SerializedName("ShortTermDeposit")
    ShortTermDeposit(title = R.string.account_short_term_deposit),
    @SerializedName("LongTermDeposit")
    LongTermDeposit(title = R.string.account_long_term_deposit),
    @SerializedName("JointAccount")
    JointAccount(title = R.string.account_joint);
}
