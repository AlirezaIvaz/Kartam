package ir.alirezaivaz.kartam.dto

import android.net.Uri

data class BackupPreview(
    val fileName: String,
    val ownedCount: Int,
    val otherCount: Int,
    val uri: Uri
)
