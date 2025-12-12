package ir.alirezaivaz.kartam

import android.app.Application
import androidx.compose.runtime.Composer
import androidx.compose.runtime.tooling.ComposeStackTraceMode

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Composer.setDiagnosticStackTraceMode(ComposeStackTraceMode.Auto)
    }
}
