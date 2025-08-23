package ir.alirezaivaz.kartam.dto

import androidx.annotation.StringRes

data class Result(
    val isSuccess: Boolean = false,
    @param:StringRes
    val message: Int? = null,
    val data: List<Any> = emptyList()
)
