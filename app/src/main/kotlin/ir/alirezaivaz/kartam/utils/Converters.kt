package ir.alirezaivaz.kartam.utils

import androidx.room.TypeConverter
import ir.alirezaivaz.kartam.dto.Bank

class Converters {
    @TypeConverter
    fun fromBank(bank: Bank): String = bank.name

    @TypeConverter
    fun toBank(bankName: String): Bank = Bank.valueOf(bankName)
}
