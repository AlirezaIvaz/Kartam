package ir.alirezaivaz.kartam.utils

import com.google.gson.Gson
import ir.alirezaivaz.kartam.App
import ir.alirezaivaz.kartam.BuildConfig
import ir.alirezaivaz.kartam.dto.Backup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

object BackupManager {
    private val db by lazy { KartamDatabase.instance }
    private val dao by lazy { db.cardDao() }
    private val gson = Gson()
    private val backupFile by lazy { File(App.appContext.noBackupFilesDir, BACKUP_FILE_NAME) }

    suspend fun backupNow() = withContext(Dispatchers.IO) {
        val cards = dao.getAll()
        val backup = Backup(
            version = BuildConfig.VERSION_CODE,
            timestamp = System.currentTimeMillis(),
            cards = cards
        )
        backupFile.writeText(gson.toJson(backup))
    }

    suspend fun restoreIfNeeded() = withContext(Dispatchers.IO) {
        if (!backupFile.exists()) return@withContext
        val backup = gson.fromJson(backupFile.readText(), Backup::class.java)
        val cardCount = dao.getCount()
        if (cardCount == 0 && backup.cards.isNotEmpty()) {
            dao.insertAll(backup.cards)
        }
    }

    private const val BACKUP_FILE_NAME = "cards_backup.json"
}
