package ir.alirezaivaz.kartam.ui.activities

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ir.alirezaivaz.kartam.ui.screens.PinSetupScreen
import ir.alirezaivaz.kartam.utils.SettingsManager

class ActivityLockSetup : KartamActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        setContent {
            PinSetupScreen(
                onFinished = {
                    SettingsManager.setLockOnStart(true)
                    finish()
                }
            )
        }
    }
}
