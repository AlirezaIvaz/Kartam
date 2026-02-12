package ir.alirezaivaz.kartam.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "cards")
data class CardInfo(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    val id: Int = 0,
    @ColumnInfo(name = "name")
    @SerializedName("name")
    val name: String,
    @ColumnInfo(name = "name_en")
    @SerializedName("name_en")
    val englishName: String? = null,
    @ColumnInfo(name = "number")
    @SerializedName("number")
    val number: String,
    @ColumnInfo(name = "shaba_number")
    @SerializedName("shaba_number")
    val shabaNumber: String?,
    @ColumnInfo(name = "account_number")
    @SerializedName("account_number")
    val accountNumber: String?,
    @ColumnInfo(name = "branch_code")
    @SerializedName("branch_code")
    val branchCode: Int? = null,
    @ColumnInfo(name = "branch_name")
    @SerializedName("branch_name")
    val branchName: String? = null,
    @ColumnInfo(name = "exp_month")
    @SerializedName("exp_month")
    val expirationMonth: Int?,
    @ColumnInfo(name = "exp_year")
    @SerializedName("exp_year")
    val expirationYear: Int?,
    @ColumnInfo(name = "cvv2")
    @SerializedName("cvv2")
    val cvv2: SensitiveString?,
    @ColumnInfo(name = "first_code")
    @SerializedName("first_code")
    val firstCode: SensitiveString? = null,
    @ColumnInfo(name = "bank")
    @SerializedName("bank")
    val bank: Bank,
    @ColumnInfo(name = "account_type")
    @SerializedName("account_type")
    val accountType: AccountType? = null,
    @ColumnInfo(name = "comment")
    @SerializedName("comment")
    val comment: String? = null,
    @ColumnInfo(name = "owned")
    @SerializedName("owned")
    val isOwned: Boolean = true,
    @ColumnInfo(name = "position")
    @SerializedName("position")
    val position: Int = 0,
)
