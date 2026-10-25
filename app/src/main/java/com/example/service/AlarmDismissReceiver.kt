package com.example.service

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.data.StudyLockManager

/**
 * BroadcastReceiver triggered by the Heads-Up notification "Stop" / "Dismiss" action button.
 * Immediately silences the MediaPlayer alarm, releases audio resources, stops vibration,
 * and clears the ongoing notification.
 */
class AlarmDismissReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val action = intent?.action
        if (action == StudyBlockerService.ACTION_STOP_ALARM ||
            action == StudyBlockerService.ACTION_DISMISS_ALARM ||
            action == StudyBlockerService.ACTION_SILENCE_ALARM
        ) {
            // Silence alarm and stop foreground service
            StudyBlockerService.silenceAlarm(context)

            // Direct fallback to ensure notification is cancelled and alarm state reset
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
            notificationManager?.cancel(StudyBlockerService.NOTIFICATION_ID)
            StudyLockManager.getInstance(context).stopAlarm()
        }
    }
}
