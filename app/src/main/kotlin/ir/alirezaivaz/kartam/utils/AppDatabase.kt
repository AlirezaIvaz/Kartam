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
abstract class AppDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE cards ADD COLUMN name_en TEXT")
            }
        }
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE cards ADD COLUMN position INTEGER NOT NULL DEFAULT 0")
                val cursor = db.query("SELECT id FROM cards ORDER BY id ASC")
                var index = 0
                while (cursor.moveToNext()) {
                    val id = cursor.getInt(0)
                    db.execSQL("UPDATE cards SET position = $index WHERE id = $id")
                    index++
                }
                cursor.close()
            }
        }

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
                .addMigrations(
                    MIGRATION_1_2,
                    MIGRATION_2_3
                )
                .build()
        }
    }
}
