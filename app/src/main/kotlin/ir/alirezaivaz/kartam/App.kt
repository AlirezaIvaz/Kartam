package ir.alirezaivaz.kartam

import android.app.Application
import androidx.compose.runtime.Composer
import androidx.compose.runtime.ExperimentalComposeRuntimeApi

class App : Application() {
    @OptIn(ExperimentalComposeRuntimeApi::class)
    override fun onCreate() {
        super.onCreate()
        Composer.setDiagnosticStackTraceEnabled(BuildConfig.DEBUG)
    }
}
