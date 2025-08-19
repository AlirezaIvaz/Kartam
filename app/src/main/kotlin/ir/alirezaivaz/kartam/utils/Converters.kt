package ir.alirezaivaz.kartam.utils

import androidx.room.TypeConverter
import ir.alirezaivaz.kartam.dto.Bank
import ir.alirezaivaz.kartam.dto.SensitiveString

class Converters {
    @TypeConverter
    fun fromBank(bank: Bank): String = bank.name

    @TypeConverter
    fun toBank(bankName: String): Bank = Bank.valueOf(bankName)

    @TypeConverter
    fun fromSensitive(value: SensitiveString?): String? {
        return value?.let { CryptoHelper.encrypt(it.value) }
    }

    @TypeConverter
    fun toSensitive(value: String?): SensitiveString? {
        return value?.let { SensitiveString(CryptoHelper.decrypt(it)) }
    }
}
