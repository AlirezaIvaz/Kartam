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

    companion object {
        const val EXTRA_ID = "id"
        const val EXTRA_OWNED = "owned"
        const val EXTRA_NAME = "name"
        const val EXTRA_CARD_NUMBER = "card_number"
        const val EXTRA_SHABA_NUMBER = "shaba_number"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFlagSecure(SettingsManager.isFlagSecure.value)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        val cardId = intent.getIntExtra(EXTRA_ID, -1)
        val isOwned = intent.getBooleanExtra(EXTRA_OWNED, true)
        val ownerName = intent.getStringExtra(EXTRA_NAME)
        val cardNumber = intent.getStringExtra(EXTRA_CARD_NUMBER).orEmpty()
        val shabaNumber = intent.getStringExtra(EXTRA_SHABA_NUMBER)
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
