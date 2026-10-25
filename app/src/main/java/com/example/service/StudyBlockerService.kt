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
        const val CHANNEL_ID_COUNTDOWN = "study_countdown_focus_channel"
        const val CHANNEL_ID_ALARM = "study_alarm_heads_up_channel"
        const val NOTIFICATION_ID = 2001

        const val ACTION_START_SESSION = "com.example.service.ACTION_START_SESSION"
        const val ACTION_STOP_ALARM = "com.example.service.ACTION_STOP_ALARM"
        const val ACTION_DISMISS_ALARM = "com.example.service.ACTION_DISMISS_ALARM"
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
                action = ACTION_STOP_ALARM
            }
            context.startService(intent)
        }
    }

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        studyLockManager = StudyLockManager.getInstance(this)
        createNotificationChannels()
        initVibrator()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_STOP_ALARM,
            ACTION_DISMISS_ALARM,
            ACTION_SILENCE_ALARM -> {
                handleStopAlarm()
            }
            else -> {
                startForegroundNotification()
                startStudyTicker()
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun startForegroundNotification() {
        val session = studyLockManager.activeSession.value
        val title = session?.chapterTitle ?: "Active Focus Session"
        val remainingSec = session?.remainingSeconds ?: (25 * 60)
        val blockedCount = session?.blockedPackages?.size ?: 0
        val isCompleted = session?.isTimerCompleted == true

        val notification = if (isCompleted) {
            buildRingingAlarmNotification()
        } else {
            buildOngoingCountdownNotification(
                title = title,
                remainingSeconds = remainingSec,
                blockedAppsCount = blockedCount
            )
        }

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
                        triggerLoudAlarmAndNotification()
                    }
                    delay(1500)
                    continue
                }

                val currentRemaining = session.remainingSeconds
                if (currentRemaining <= 0) {
                    // Timer reached 00:00! Mark completed and elevate to Heads-Up alarm
                    studyLockManager.updateSessionCountdown(0, isCompleted = true)
                    triggerLoudAlarmAndNotification()
                    delay(1500)
                    continue
                }

                // Decrement remaining seconds
                val newRemaining = currentRemaining - 1
                studyLockManager.updateSessionCountdown(newRemaining, isCompleted = false)

                // Update persistent notification with live countdown
                val ongoingNotification = buildOngoingCountdownNotification(
                    title = session.chapterTitle,
                    remainingSeconds = newRemaining,
                    blockedAppsCount = session.blockedPackages.size
                )
                notificationManager.notify(NOTIFICATION_ID, ongoingNotification)

                // Real-time App Blocker: inspect currently running foreground application
                if (session.blockedPackages.isNotEmpty() && AppBlockerHelper.isUsageAccessGranted(this@StudyBlockerService)) {
                    val fgPackage = AppBlockerHelper.getForegroundPackageName(this@StudyBlockerService)
                    if (fgPackage != null && session.blockedPackages.contains(fgPackage) && fgPackage != packageName) {
                        // Distraction detected! Instantly redirect back to STUDYPAL screen
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

        // Bring STUDYPAL to the foreground immediately with FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TOP
        val launchIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra(EXTRA_BLOCKED_APP_KEY, appName)
        }
        startActivity(launchIntent)
    }

    private fun triggerLoudAlarmAndNotification() {
        if (isAlarmActive) return
        isAlarmActive = true

        // Play loud repeating alarm sound
        try {
            val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

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

        // Repeating vibration pattern
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

        // Elevate notification to Heads-Up Priority with explicit Stop / Dismiss Action
        val headsUpNotification = buildRingingAlarmNotification()
        notificationManager.notify(NOTIFICATION_ID, headsUpNotification)
    }

    private fun handleStopAlarm() {
        isAlarmActive = false

        // 1. Immediately stop MediaPlayer and release audio resources
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        } catch (_: Exception) {}

        // 2. Stop vibration
        try {
            vibrator?.cancel()
        } catch (_: Exception) {}

        // 3. Update StudyLockManager alarm state
        studyLockManager.stopAlarm()

        // 4. Cancel and clear the ongoing notification immediately
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        } else {
            @Suppress("DEPRECATION")
            stopForeground(true)
        }
        notificationManager.cancel(NOTIFICATION_ID)

        // 5. Cancel ticker job and terminate service
        tickerJob?.cancel()
        stopSelf()
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

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 1. Low-importance silent channel for 1-second countdown updates
            val countdownChannel = NotificationChannel(
                CHANNEL_ID_COUNTDOWN,
                "Study Timer Countdown",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Ongoing silent countdown display for active study sessions."
                setShowBadge(false)
                setSound(null, null)
                enableVibration(false)
            }
            notificationManager.createNotificationChannel(countdownChannel)

            // 2. High-importance channel for ringing Heads-Up Alarm upon timer completion
            val alarmChannel = NotificationChannel(
                CHANNEL_ID_ALARM,
                "Study Timer Alarm Alert",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "High priority Heads-Up alert when study session ends."
                setShowBadge(true)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(alarmChannel)
        }
    }

    private fun formatTime(seconds: Int): String {
        val mins = seconds / 60
        val secs = seconds % 60
        return String.format(Locale.US, "%02d:%02d", mins, secs)
    }

    @SuppressLint("NotificationTrampoline")
    private fun buildOngoingCountdownNotification(
        title: String,
        remainingSeconds: Int,
        blockedAppsCount: Int
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

        return NotificationCompat.Builder(this, CHANNEL_ID_COUNTDOWN)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle("🔒 Study Focus: $title")
            .setContentText("Time remaining: ${formatTime(remainingSeconds)} • $blockedAppsCount apps blocked")
            .setSubText("Non-Cancellable Session")
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_PROGRESS)
            .build()
    }

    @SuppressLint("NotificationTrampoline")
    private fun buildRingingAlarmNotification(): Notification {
        val openAppIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Stop / Dismiss PendingIntent targeting AlarmDismissReceiver and Service action
        val stopIntent = Intent(this, AlarmDismissReceiver::class.java).apply {
            action = ACTION_STOP_ALARM
        }
        val stopPendingIntent = PendingIntent.getBroadcast(
            this,
            1,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val dismissIntent = Intent(this, AlarmDismissReceiver::class.java).apply {
            action = ACTION_DISMISS_ALARM
        }
        val dismissPendingIntent = PendingIntent.getBroadcast(
            this,
            2,
            dismissIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID_ALARM)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle("⏰ Study Session Ended!")
            .setContentText("Timer reached 00:00! Tap Stop to silence alarm and unlock next steps.")
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Stop", stopPendingIntent)
            .addAction(android.R.drawable.ic_lock_idle_alarm, "Dismiss", dismissPendingIntent)
            .build()
    }

    override fun onDestroy() {
        handleStopAlarm()
        super.onDestroy()
    }
}
