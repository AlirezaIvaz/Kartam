package ir.alirezaivaz.kartam.dto

data class Backup(
    val version: Int,
    val timestamp: Long,
    val cards: List<CardInfo>
)
