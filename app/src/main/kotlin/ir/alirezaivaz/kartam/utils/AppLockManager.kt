package ir.alirezaivaz.kartam.utils

import android.content.Intent
import android.os.SystemClock
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import ir.alirezaivaz.kartam.App
import ir.alirezaivaz.kartam.ui.activities.ActivityLock

/**
 * Re-locks the app when it returns to the foreground after having been in the
 * background for longer than the configured grace period.
 *
 * The decision is driven by [ProcessLifecycleOwner] so it only reacts to the
 * *whole app* going to the background — navigating between our own activities
 * (e.g. [ActivityLock] -> ActivityMain -> AddCardActivity) never triggers it.
 *
 * Cold start is handled by ActivitySplash, so the first foreground after process
 * launch is intentionally ignored here to avoid double-locking.
 */
object AppLockManager : DefaultLifecycleObserver {

    private var lastBackgroundedAt = 0L
    private var wasBackgrounded = false

    fun init() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun onStop(owner: LifecycleOwner) {
        lastBackgroundedAt = SystemClock.elapsedRealtime()
        wasBackgrounded = true
    }

    override fun onStart(owner: LifecycleOwner) {
        if (!wasBackgrounded) return
        if (!shouldLock()) return
        val context = App.appContext
        context.startActivity(
            Intent(context, ActivityLock::class.java).apply {
                putExtra(ActivityLock.EXTRA_RETURN_ONLY, true)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        )
    }

    private fun shouldLock(): Boolean {
        if (!SettingsManager.isLockOnStart.value) return false
        if (!SettingsManager.isLockOnReturn.value) return false
        val graceMillis = SettingsManager.lockGraceSeconds.value * 1000L
        val elapsed = SystemClock.elapsedRealtime() - lastBackgroundedAt
        return elapsed >= graceMillis
    }
}
