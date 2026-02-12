package ir.alirezaivaz.kartam.dto

import com.google.gson.annotations.SerializedName

data class Backup(
    @SerializedName("version")
    val version: Int,
    @SerializedName("timestamp")
    val timestamp: Long,
    @SerializedName("cards")
    val cards: List<CardInfo>
)
