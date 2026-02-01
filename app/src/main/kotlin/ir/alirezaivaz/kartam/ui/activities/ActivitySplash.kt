package ir.alirezaivaz.kartam.ui.activities

import android.content.Intent
import android.os.Bundle
import ir.alirezaivaz.kartam.utils.SettingsManager

class ActivitySplash : KartamActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val destination = if (SettingsManager.isLockOnStart.value) {
            ActivityLock::class.java
        } else {
            ActivityMain::class.java
        }
        startActivity(
            Intent(
                this@ActivitySplash,
                destination
            )
        )
        finish()
    }
}
