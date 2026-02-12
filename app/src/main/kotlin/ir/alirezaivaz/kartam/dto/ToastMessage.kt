package ir.alirezaivaz.kartam.dto

import androidx.annotation.StringRes
import com.dokar.sonner.ToastType

data class ToastMessage(
    @param:StringRes
    val message: Int,
    val type: ToastType = ToastType.Error
)
