package ir.alirezaivaz.kartam.dto

import android.net.Uri
import ir.mehrafzoon.composedatepicker.dto.MixedCalendar

data class BackupFile(
    val name: String,
    val uri: Uri,
    val date: MixedCalendar
)
