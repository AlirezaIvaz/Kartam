package ir.alirezaivaz.kartam.utils

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ir.alirezaivaz.kartam.dao.CardDao
import ir.alirezaivaz.kartam.dto.CardInfo

@Database(entities = [CardInfo::class], version = 3)
@TypeConverters(Converters::class)
abstract class KartamDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao

    companion object {
        @Volatile
        private var instance: KartamDatabase? = null
        private const val DATABASE_FILE_NAME = "kartam"
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE cards ADD COLUMN owned INTEGER NOT NULL DEFAULT 1")
            }
        }
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE cards ADD COLUMN account_type TEXT")
            }
        }

        fun getInstance(context: Context): KartamDatabase {
            return instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context): KartamDatabase {
            return Room
                .databaseBuilder(
                    context = context,
                    klass = KartamDatabase::class.java,
                    name = DATABASE_FILE_NAME
                )
                .addMigrations(
                    MIGRATION_1_2,
                    MIGRATION_2_3
                )
                .build()
        }
    }
}
