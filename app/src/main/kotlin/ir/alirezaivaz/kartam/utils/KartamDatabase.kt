package ir.alirezaivaz.kartam.utils

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ir.alirezaivaz.kartam.dto.CardItem
import ir.alirezaivaz.kartam.dto.CardsDao
import java.io.File

@Database(entities = [CardItem::class], version = 1)
@TypeConverters(Converters::class)
abstract class KartamDatabase : RoomDatabase() {
    abstract fun cardDao(): CardsDao

    companion object {
        @Volatile
        private var instance: KartamDatabase? = null
        private const val DATABASE_FILE_NAME = "kartam"

        fun getInstance(context: Context): KartamDatabase {
            return instance ?: buildDatabase(context).also { instance = it }
        }

        fun getDatabaseFile(context: Context): File {
            return context.getDatabasePath(DATABASE_FILE_NAME)
        }

        private fun buildDatabase(context: Context): KartamDatabase {
            val dbFile = getDatabaseFile(context)

            return Room.databaseBuilder(
                context = context,
                klass = KartamDatabase::class.java,
                name = dbFile.absolutePath
            ).build()
        }
    }
}
