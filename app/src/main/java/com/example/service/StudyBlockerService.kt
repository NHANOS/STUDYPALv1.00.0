package com.example.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.data.StudyLockManager
import com.example.util.AppBlockerHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Locale

class StudyBlockerService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.Default)
    private var tickerJob: Job? = null
    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null
    private var isAlarmActive = false

    private lateinit var notificationManager: NotificationManager
    private lateinit var studyLockManager: StudyLockManager

    companion object {
        const val CHANNEL_ID = "study_countdown_focus_channel"
        const val NOTIFICATION_ID = 2001

        const val ACTION_START_SESSION = "com.example.service.ACTION_START_SESSION"
        const val ACTION_SILENCE_ALARM = "com.example.service.ACTION_SILENCE_ALARM"

        const val EXTRA_BLOCKED_APP_KEY = "extra_blocked_app_detected"

        fun startService(context: Context) {
            val intent = Intent(context, StudyBlockerService::class.java).apply {
                action = ACTION_START_SESSION
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun silenceAlarm(context: Context) {
            val intent = Intent(context, StudyBlockerService::class.java).apply {
                action = ACTION_SILENCE_ALARM
            }
            context.startService(intent)
        }
    }

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        studyLockManager = StudyLockManager.getInstance(this)
        createNotificationChannel()
        initVibrator()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_SILENCE_ALARM -> {
                stopLoudAlarm()
                studyLockManager.stopAlarm()
            }
            else -> {
                startForegroundNotification()
                startStudyTicker()
            }
        }
        // START_STICKY ensures Android recreates the service if system kills it under memory pressure
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun startForegroundNotification() {
        val session = studyLockManager.activeSession.value
        val title = session?.chapterTitle ?: "Active Focus Session"
        val remainingSec = session?.remainingSeconds ?: (25 * 60)
        val blockedCount = session?.blockedPackages?.size ?: 0

        val notification = buildOngoingNotification(
            title = title,
            remainingSeconds = remainingSec,
            blockedAppsCount = blockedCount,
            isCompleted = session?.isTimerCompleted == true
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
            } else {
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            }
            startForeground(NOTIFICATION_ID, notification, type)
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }
    }

    private fun startStudyTicker() {
        if (tickerJob?.isActive == true) return

        tickerJob = serviceScope.launch {
            while (isActive) {
                val session = studyLockManager.activeSession.value ?: break

                if (session.isTimerCompleted) {
                    if (!isAlarmActive) {
                        triggerLoudAlarm()
                    }
                    delay(2000)
                    continue
                }

                val currentRemaining = session.remainingSeconds
                if (currentRemaining <= 0) {
                    // Timer reached 00:00!
                    studyLockManager.updateSessionCountdown(0, isCompleted = true)
                    triggerLoudAlarm()
                    updateNotification(
                        title = session.chapterTitle,
                        remainingSeconds = 0,
                        blockedAppsCount = session.blockedPackages.size,
                        isCompleted = true
                    )
                    delay(2000)
                    continue
                }

                // Decrement remaining seconds
                val newRemaining = currentRemaining - 1
                studyLockManager.updateSessionCountdown(newRemaining, isCompleted = false)

                // Update notification every 1 second
                updateNotification(
                    title = session.chapterTitle,
                    remainingSeconds = newRemaining,
                    blockedAppsCount = session.blockedPackages.size,
                    isCompleted = false
                )

                // App Blocker Check: inspect currently running foreground app
                if (session.blockedPackages.isNotEmpty() && AppBlockerHelper.isUsageAccessGranted(this@StudyBlockerService)) {
                    val fgPackage = AppBlockerHelper.getForegroundPackageName(this@StudyBlockerService)
                    if (fgPackage != null && session.blockedPackages.contains(fgPackage) && fgPackage != packageName) {
                        // Blocked app detected! Re-launch study focus screen
                        handleBlockedAppEncountered(fgPackage)
                    }
                }

                delay(1000)
            }
        }
    }

    private fun handleBlockedAppEncountered(blockedPackage: String) {
        val appName = try {
            val appInfo = packageManager.getApplicationInfo(blockedPackage, 0)
            packageManager.getApplicationLabel(appInfo).toString()
        } catch (_: Exception) {
            blockedPackage
        }

        studyLockManager.notifyBlockedAppTriggered(appName)

        // Bring our app to foreground to enforce the study session
        val launchIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra(EXTRA_BLOCKED_APP_KEY, appName)
        }
        startActivity(launchIntent)
    }

    private fun triggerLoudAlarm() {
        if (isAlarmActive) return
        isAlarmActive = true

        try {
            val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)

            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(applicationContext, alarmUri)
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                isLooping = true
                prepare()
                start()
            }
        } catch (e: Exception) {
            Log.e("StudyBlockerService", "Failed to play alarm: ${e.message}")
        }

        // Start repeating vibration pattern
        try {
            val pattern = longArrayOf(0, 800, 400, 800, 400, 1000)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createWaveform(pattern, 0))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(pattern, 0)
            }
        } catch (e: Exception) {
            Log.e("StudyBlockerService", "Failed to vibrate: ${e.message}")
        }
    }

    private fun stopLoudAlarm() {
        isAlarmActive = false
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        } catch (_: Exception) {}

        try {
            vibrator?.cancel()
        } catch (_: Exception) {}
    }

    private fun initVibrator() {
        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vm = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vm?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Study Focus & App Blocker",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Displays live countdown for non-cancellable study sessions and enforces app blocking."
                setShowBadge(true)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun formatTime(seconds: Int): String {
        val mins = seconds / 60
        val secs = seconds % 60
        return String.format(Locale.US, "%02d:%02d", mins, secs)
    }

    @SuppressLint("NotificationTrampoline")
    private fun buildOngoingNotification(
        title: String,
        remainingSeconds: Int,
        blockedAppsCount: Int,
        isCompleted: Boolean
    ): Notification {
        val openAppIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val silenceIntent = Intent(this, StudyBlockerService::class.java).apply {
            action = ACTION_SILENCE_ALARM
        }
        val silencePendingIntent = PendingIntent.getService(
            this,
            1,
            silenceIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentIntent(pendingIntent)
            .setOngoing(!isCompleted)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)

        if (isCompleted) {
            builder.setContentTitle("🎉 Study Session Completed!")
                .setContentText("⏰ Loud alarm ringing! Tap to complete chapter & unlock next steps.")
                .addAction(android.R.drawable.ic_lock_idle_alarm, "Silence Alarm", silencePendingIntent)
        } else {
            builder.setContentTitle("🔒 Study Focus: $title")
                .setContentText("Time remaining: ${formatTime(remainingSeconds)} • $blockedAppsCount apps blocked")
                .setSubText("Non-Cancellable Session")
        }

        return builder.build()
    }

    private fun updateNotification(
        title: String,
        remainingSeconds: Int,
        blockedAppsCount: Int,
        isCompleted: Boolean
    ) {
        val notification = buildOngoingNotification(title, remainingSeconds, blockedAppsCount, isCompleted)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    override fun onDestroy() {
        stopLoudAlarm()
        tickerJob?.cancel()
        super.onDestroy()
    }
}
