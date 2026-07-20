package ir.alirezaivaz.kartam

import android.app.Application
import android.content.Context
import androidx.compose.runtime.Composer
import androidx.compose.runtime.tooling.ComposeStackTraceMode
import ir.alirezaivaz.kartam.utils.AppLockManager

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        Composer.setDiagnosticStackTraceMode(ComposeStackTraceMode.Auto)
        AppLockManager.init()
    }

    companion object {
        lateinit var appContext: Context
            private set
        const val DEFAULT_PIN_LENGTH = 6
        const val SUPPORT_URL = "https://t.me/AlirezaIvaz"
        const val TELEGRAM_CHANNEL_URL = "https://t.me/KartamApp"
    }
}
