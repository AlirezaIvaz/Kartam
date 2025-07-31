package ir.alirezaivaz.kartam.utils

import androidx.sqlite.db.SupportSQLiteOpenHelper
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory

class DisposableKeySupportFactory(private val decryptedKey: ByteArray) :
    SupportOpenHelperFactory(decryptedKey) {

    override fun create(configuration: SupportSQLiteOpenHelper.Configuration): SupportSQLiteOpenHelper {
        val helper = super.create(configuration)
        decryptedKey.fill(0)
        return helper
    }
}
