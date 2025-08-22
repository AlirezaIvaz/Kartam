package ir.alirezaivaz.kartam

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ir.alirezaivaz.kartam.ui.screens.AddCardScreen

class AddCardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        val cardId = intent.getIntExtra("id", -1)
        val isOwned = intent.getBooleanExtra("owned", true)
        setContent {
            AddCardScreen(
                cardId = cardId,
                isOwned = isOwned
            )
        }
    }
}
