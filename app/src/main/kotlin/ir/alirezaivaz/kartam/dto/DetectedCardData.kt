package ir.alirezaivaz.kartam.dto

import kotlinx.serialization.Serializable

@Serializable
data class DetectedCardData(
    val name: String?,
    val cardNumber: String?,
    val shabaNumber: String?,
)
