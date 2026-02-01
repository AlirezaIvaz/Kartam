package ir.alirezaivaz.kartam.ui.activities

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
}
