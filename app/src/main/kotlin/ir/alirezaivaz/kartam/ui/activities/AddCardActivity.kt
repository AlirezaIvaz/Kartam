package ir.alirezaivaz.kartam.ui.activities

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ir.alirezaivaz.kartam.dto.DetectedCardData
import ir.alirezaivaz.kartam.ui.screens.AddCardScreen
import ir.alirezaivaz.kartam.utils.SettingsManager

class AddCardActivity : KartamActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFlagSecure(SettingsManager.isFlagSecure.value)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        val cardId = intent.getIntExtra("id", -1)
        val isOwned = intent.getBooleanExtra("owned", true)
        val ownerName = intent.getStringExtra("name")
        val cardNumber = intent.getStringExtra("card_number").orEmpty()
        val shabaNumber = intent.getStringExtra("shaba_number")
        val detectedCardData = DetectedCardData(
            name = ownerName,
            cardNumber = cardNumber,
            shabaNumber = shabaNumber
        )
        setContent {
            AddCardScreen(
                cardId = cardId,
                isOwned = isOwned,
                detectedCardData = detectedCardData
            )
        }
    }
}
