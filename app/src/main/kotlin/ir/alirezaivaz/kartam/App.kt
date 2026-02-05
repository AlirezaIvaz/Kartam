package ir.alirezaivaz.kartam

import android.app.Application
import android.content.Context
import androidx.compose.runtime.Composer
import androidx.compose.runtime.tooling.ComposeStackTraceMode

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        Composer.setDiagnosticStackTraceMode(ComposeStackTraceMode.Auto)
    }

    companion object {
        lateinit var appContext: Context
            private set
        const val DEFAULT_PIN_LENGTH = 6
    }
}
