package ir.alirezaivaz.kartam.dto

data class ChangelogVersion(
    val version: String,
    val date: String,
    val items: List<ChangelogItem>
)
