package ir.alirezaivaz.kartam.utils

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Base64
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import ir.alirezaivaz.kartam.App
import ir.alirezaivaz.kartam.BuildConfig
import ir.alirezaivaz.kartam.R
import ir.alirezaivaz.kartam.dto.Backup
import ir.alirezaivaz.kartam.dto.BackupFile
import ir.alirezaivaz.kartam.extensions.takeDigits
import ir.mehrafzoon.composedatepicker.dto.toMixedCalendar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Locale

object BackupManager {
    private val db by lazy { KartamDatabase.instance }
    private val dao by lazy { db.cardDao() }
    private val gson = Gson()
    private val backupFile by lazy { File(App.appContext.noBackupFilesDir, BACKUP_FILE_NAME) }

    suspend fun getOwnedCount(): Int = withContext(Dispatchers.IO) {
        dao.getOwnedCount()
    }

    suspend fun getOthersCount(): Int = withContext(Dispatchers.IO) {
        dao.getOthersCount()
    }

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
        runCatching {
            val cardCount = dao.getCount()
            if (cardCount == 0) {
                val backup = gson.fromJson(backupFile.readText(), Backup::class.java)
                if (backup.cards.isNotEmpty()) {
                    dao.insertAll(backup.cards)
                }
            }
        }
    }

    fun getBackupFolderPath(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            "$DOWNLOAD_FOLDER_NAME/$BACKUP_FOLDER_NAME"
        } else {
            val downloads =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            "${downloads.absolutePath}/$BACKUP_FOLDER_NAME"
        }
    }

    suspend fun createManualBackup(): Boolean {
        val data = createBackupBytes()
        val fileName = MANUAL_BACKUP_FILE_NAME.format(
            Locale.ENGLISH,
            System.currentTimeMillis(),
            MANUAL_BACKUP_FILE_EXTENSION
        )

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveBackupToDownloads(fileName, data) != null
        } else {
            saveBackupLegacy(fileName, data)
        }
    }

    suspend fun createBackupBytes(): ByteArray {
        val backup = Backup(
            version = BuildConfig.VERSION_CODE,
            timestamp = System.currentTimeMillis(),
            cards = dao.getAll()
        )
        val json = gson.toJson(backup)
        val base64 = Base64.encodeToString(
            json.toByteArray(Charsets.UTF_8),
            Base64.NO_WRAP
        )
        return base64.toByteArray(Charsets.UTF_8)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun saveBackupToDownloads(
        fileName: String,
        data: ByteArray
    ): Uri? {
        val resolver = App.appContext.contentResolver

        val values = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, fileName)
            put(MediaStore.Downloads.MIME_TYPE, "application/octet-stream")
            put(
                MediaStore.Downloads.RELATIVE_PATH,
                getBackupFolderPath()
            )
            put(MediaStore.Downloads.IS_PENDING, 1)
        }

        val uri = resolver.insert(
            MediaStore.Downloads.EXTERNAL_CONTENT_URI,
            values
        ) ?: return null

        resolver.openOutputStream(uri)?.use { out ->
            out.write(data)
        }

        values.clear()
        values.put(MediaStore.Downloads.IS_PENDING, 0)
        resolver.update(uri, values, null, null)

        return uri
    }

    fun saveBackupLegacy(
        fileName: String,
        data: ByteArray
    ): Boolean {
        return try {
            val dir = File(getBackupFolderPath())
            if (!dir.exists()) dir.mkdirs()
            val file = File(dir, fileName)
            file.writeBytes(data)
            MediaScannerConnection.scanFile(
                App.appContext,
                arrayOf(file.absolutePath),
                null,
                null
            )
            true
        } catch (_: Exception) {
            false
        }
    }

    fun listBackups(): List<BackupFile> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            listBackupFiles()
        } else {
            listBackupsLegacy()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun listBackupFiles(): List<BackupFile> {
        val resolver = App.appContext.contentResolver
        val result = mutableListOf<BackupFile>()

        val projection = arrayOf(
            MediaStore.Downloads._ID,
            MediaStore.Downloads.DISPLAY_NAME,
            MediaStore.Downloads.SIZE,
            MediaStore.Downloads.DATE_MODIFIED
        )

        val selection = """
        ${MediaStore.Downloads.RELATIVE_PATH} = ?
        AND ${MediaStore.Downloads.DISPLAY_NAME} LIKE ?
    """.trimIndent()

        val selectionArgs = arrayOf(
            "${getBackupFolderPath()}/",
            "%.$MANUAL_BACKUP_FILE_EXTENSION"
        )

        resolver.query(
            MediaStore.Downloads.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            "${MediaStore.Downloads.DATE_MODIFIED} DESC"
        )?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Downloads._ID)
            val nameCol = cursor.getColumnIndexOrThrow(MediaStore.Downloads.DISPLAY_NAME)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idCol)
                val name = cursor.getString(nameCol)
                val uri = ContentUris.withAppendedId(MediaStore.Downloads.EXTERNAL_CONTENT_URI, id)
                val date = name.takeDigits().toLongOrNull() ?: 0L

                result += BackupFile(
                    name = name,
                    uri = uri,
                    date = date.toMixedCalendar()
                )
            }
        }

        return result
    }

    fun listBackupsLegacy(): List<BackupFile> {
        val dir = File(getBackupFolderPath())
        if (!dir.exists()) return emptyList()

        return dir.listFiles { file ->
            file.extension == MANUAL_BACKUP_FILE_EXTENSION
        }?.map {
            val date = it.nameWithoutExtension.takeDigits().toLongOrNull() ?: 0L
            BackupFile(
                name = it.name,
                uri = Uri.fromFile(it),
                date = date.toMixedCalendar()
            )
        }?.sortedByDescending { it.date.timestamp }
            ?: emptyList()
    }

    fun shareBackup(context: Context, backupFile: BackupFile) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/octet-stream"
            putExtra(Intent.EXTRA_STREAM, backupFile.uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(
            Intent.createChooser(intent, context.getString(R.string.action_share_via))
        )
    }

    suspend fun restoreBackup(backup: Backup) {
        db.clearAllTables()
        dao.insertAll(backup.cards)
    }

    fun readBackupFromUri(uri: Uri): Backup {
        val name = queryFileName(uri)
        if (name?.endsWith(".$MANUAL_BACKUP_FILE_EXTENSION") != true) {
            throw UnsupportedOperationException()
        }

        val base64 = App.appContext.contentResolver
            .openInputStream(uri)
            ?.bufferedReader()
            ?.use { it.readText() }

        val json = String(
            Base64.decode(base64, Base64.NO_WRAP),
            Charsets.UTF_8
        )

        return Gson().fromJson(json, Backup::class.java)
    }

    fun queryFileName(uri: Uri): String? {
        return App.appContext.contentResolver
            .query(
                uri,
                arrayOf(OpenableColumns.DISPLAY_NAME),
                null,
                null,
                null
            )
            ?.use { cursor ->
                if (cursor.moveToFirst()) {
                    cursor.getString(0)
                } else null
            }
    }

    private const val BACKUP_FILE_NAME = "cards_backup.json"
    private const val DOWNLOAD_FOLDER_NAME = "Download"
    private const val BACKUP_FOLDER_NAME = "Kartam/Backups"
    private const val MANUAL_BACKUP_FILE_NAME = "KartamBackup-%d.%s"
    private const val MANUAL_BACKUP_FILE_EXTENSION = "ktb"
}
