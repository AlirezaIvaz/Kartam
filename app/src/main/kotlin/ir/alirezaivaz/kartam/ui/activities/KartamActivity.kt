package ir.alirezaivaz.kartam.ui.activities

import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import ir.alirezaivaz.kartam.dto.Language
import ir.alirezaivaz.kartam.utils.SettingsManager
import ir.alirezaivaz.kartam.utils.Utils

open class KartamActivity : AppCompatActivity() {
    override fun onResume() {
        syncSystemLanguage()
        super.onResume()
    }

    fun syncSystemLanguage() {
        val appLanguage = SettingsManager.getLanguage()
        val deviceLanguage = Utils.getDeviceLanguage(this)
        if (appLanguage != deviceLanguage) {
            val language = Language.find(deviceLanguage)
            SettingsManager.setLanguage(language, isFromUser = false)
        }
    }

    fun setFlagSecure(enabled: Boolean) {
        if (enabled) {
            window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }
}
