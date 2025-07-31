package ir.alirezaivaz.kartam.utils

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ir.alirezaivaz.kartam.dao.CardDao
import ir.alirezaivaz.kartam.dto.CardInfo

@Database(entities = [CardInfo::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            System.loadLibrary("sqlcipher")
            val sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
            val sqlCipherKeyManager = SqlCipherKeyManager(sharedPreferences)
            val dbFile = context.getDatabasePath("kartam.db")

            return Room.databaseBuilder(
                context = context,
                klass = AppDatabase::class.java,
                name = dbFile.absolutePath
            ).openHelperFactory(sqlCipherKeyManager.getSupportFactory())
                .build()
        }
    }
}
