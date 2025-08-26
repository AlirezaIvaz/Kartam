package ir.alirezaivaz.kartam.utils

import com.google.gson.Gson
import ir.alirezaivaz.kartam.BuildConfig
import ir.alirezaivaz.kartam.dto.Backup
import java.io.File

class BackupManager(
    private val backupDir: File,
    private val db: KartamDatabase
) {
    private val dao = db.cardDao()
    private val gson = Gson()
    private val backupFile = File(backupDir, BACKUP_FILE_NAME)

    suspend fun backupNow() {
        val cards = dao.getAll()
        val backup = Backup(
            version = BuildConfig.VERSION_CODE,
            timestamp = System.currentTimeMillis(),
            cards = cards
        )
        backupFile.writeText(gson.toJson(backup))
    }

    suspend fun restoreIfNeeded() {
        if (!backupFile.exists()) return
        val backup = gson.fromJson(backupFile.readText(), Backup::class.java)
        val cardCount = dao.getCount()
        if (cardCount == 0 && backup.cards.isNotEmpty()) {
            dao.insertAll(backup.cards)
        }
    }

    companion object {
        private var _instance: BackupManager? = null
        private const val BACKUP_FILE_NAME = "cards_backup.json"
        fun getInstance(backupDir: File, db: KartamDatabase): BackupManager {
            if (_instance == null) {
                _instance = BackupManager(backupDir, db)
            }
            return _instance!!
        }
    }
}
