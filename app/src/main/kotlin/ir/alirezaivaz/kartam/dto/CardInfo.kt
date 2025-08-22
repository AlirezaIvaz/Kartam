package ir.alirezaivaz.kartam.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cards")
data class CardInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "name_en")
    val englishName: String? = null,
    @ColumnInfo(name = "number")
    val number: String,
    @ColumnInfo(name = "shaba_number")
    val shabaNumber: String?,
    @ColumnInfo(name = "account_number")
    val accountNumber: String?,
    @ColumnInfo(name = "branch_code")
    val branchCode: Int? = null,
    @ColumnInfo(name = "branch_name")
    val branchName: String? = null,
    @ColumnInfo(name = "exp_month")
    val expirationMonth: Int?,
    @ColumnInfo(name = "exp_year")
    val expirationYear: Int?,
    @ColumnInfo(name = "cvv2")
    val cvv2: SensitiveString?,
    @ColumnInfo(name = "bank")
    val bank: Bank,
    @ColumnInfo(name = "owned")
    val isOwned: Boolean = true,
    @ColumnInfo(name = "position")
    val position: Int = 0,
)
