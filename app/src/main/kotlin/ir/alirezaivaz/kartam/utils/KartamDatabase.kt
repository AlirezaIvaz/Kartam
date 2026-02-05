package ir.alirezaivaz.kartam.utils

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ir.alirezaivaz.kartam.App
import ir.alirezaivaz.kartam.dao.CardDao
import ir.alirezaivaz.kartam.dto.CardInfo

@Database(
    entities = [CardInfo::class],
    version = 4,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class KartamDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao

    companion object {
        val instance: KartamDatabase by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            buildDatabase()
        }

        private const val DATABASE_FILE_NAME = "kartam"
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE cards ADD COLUMN owned INTEGER NOT NULL DEFAULT 1")
            }
        }
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE cards ADD COLUMN account_type TEXT")
            }
        }
        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE cards ADD COLUMN first_code TEXT")
                db.execSQL("ALTER TABLE cards ADD COLUMN comment TEXT")
            }
        }

        private fun buildDatabase(): KartamDatabase {
            return Room
                .databaseBuilder(
                    context = App.appContext,
                    klass = KartamDatabase::class.java,
                    name = DATABASE_FILE_NAME
                )
                .addMigrations(
                    MIGRATION_1_2,
                    MIGRATION_2_3,
                    MIGRATION_3_4,
                )
                .build()
        }
    }
}
