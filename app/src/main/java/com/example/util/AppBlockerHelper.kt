package com.example.util

import android.app.AppOpsManager
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Process
import android.provider.Settings

data class InstalledAppItem(
    val packageName: String,
    val appName: String,
    val isDistraction: Boolean
)

object AppBlockerHelper {

    // Common distraction packages on Android (social media, entertainment, streaming, messaging, games)
    val COMMON_DISTRACTIONS = setOf(
        "com.google.android.youtube",
        "com.instagram.android",
        "com.whatsapp",
        "com.facebook.katana",
        "com.facebook.orca",
        "com.twitter.android",
        "com.snapchat.android",
        "com.netflix.mediaclient",
        "com.android.chrome",
        "com.google.android.apps.tachyon",
        "org.telegram.messenger",
        "com.reddit.frontpage",
        "com.zhiliaoapp.musically", // TikTok
        "com.spotify.music",
        "com.amazon.avod.thirdpartyclient", // Prime Video
        "com.disney.disneyplus",
        "com.supercell.clashofclans",
        "com.supercell.brawlstars",
        "com.dts.freefireth",
        "com.tencent.ig",
        "com.activision.callofduty.shooter"
    )

    /**
     * Fetch user-accessible installed applications that have a launch intent,
     * using PackageManager.getInstalledApplications(PackageManager.GET_META_DATA)
     * and filtering with packageManager.getLaunchIntentForPackage(packageName) != null.
     */
    fun getInstalledLaunchableApps(context: Context): List<InstalledAppItem> {
        val pm = context.packageManager

        @Suppress("DEPRECATION")
        val installedApplications = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pm.getInstalledApplications(PackageManager.ApplicationInfoFlags.of(PackageManager.GET_META_DATA.toLong()))
        } else {
            pm.getInstalledApplications(PackageManager.GET_META_DATA)
        }

        val myPackageName = context.packageName
        val appList = mutableListOf<InstalledAppItem>()
        val seenPackages = mutableSetOf<String>()

        for (appInfo in installedApplications) {
            val pkg = appInfo.packageName
            if (pkg == myPackageName || seenPackages.contains(pkg)) {
                continue
            }

            // Strictly filter with getLaunchIntentForPackage to ensure the app is launchable by the user
            if (pm.getLaunchIntentForPackage(pkg) == null) {
                continue
            }
            seenPackages.add(pkg)

            val name = try {
                pm.getApplicationLabel(appInfo).toString()
            } catch (_: Exception) {
                pkg
            }

            val isDistraction = COMMON_DISTRACTIONS.contains(pkg) ||
                    name.contains("youtube", ignoreCase = true) ||
                    name.contains("instagram", ignoreCase = true) ||
                    name.contains("game", ignoreCase = true) ||
                    name.contains("video", ignoreCase = true) ||
                    name.contains("chat", ignoreCase = true)

            appList.add(
                InstalledAppItem(
                    packageName = pkg,
                    appName = name,
                    isDistraction = isDistraction
                )
            )
        }

        // Sort: Common distractions first, then alphabetically
        return appList.sortedWith(
            compareByDescending<InstalledAppItem> { it.isDistraction }
                .thenBy { it.appName.lowercase() }
        )
    }

    /**
     * Check if PACKAGE_USAGE_STATS permission has been granted by the user.
     */
    @Suppress("DEPRECATION")
    fun isUsageAccessGranted(context: Context): Boolean {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as? AppOpsManager ?: return false
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOps.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(),
                context.packageName
            )
        } else {
            appOps.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(),
                context.packageName
            )
        }
        return mode == AppOpsManager.MODE_ALLOWED
    }

    /**
     * Check if SYSTEM_ALERT_WINDOW (display over other apps / overlay) permission is granted.
     */
    fun isOverlayPermissionGranted(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(context)
        } else {
            true
        }
    }

    /**
     * Intent to open the Usage Access settings screen.
     */
    fun getUsageAccessSettingsIntent(): Intent {
        return Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
    }

    /**
     * Intent to open the Overlay (Draw over other apps) settings screen for this app.
     */
    fun getOverlaySettingsIntent(context: Context): Intent {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${context.packageName}")
            ).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        } else {
            Intent(Settings.ACTION_SETTINGS).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        }
    }

    /**
     * Query the top foreground package using UsageEvents (most reliable across Android 8-15).
     */
    @Suppress("DEPRECATION")
    fun getForegroundPackageName(context: Context): String? {
        val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as? UsageStatsManager
            ?: return null

        val endTime = System.currentTimeMillis()
        val startTime = endTime - 10000 // look back 10 seconds

        val usageEvents = usageStatsManager.queryEvents(startTime, endTime) ?: return null
        val event = UsageEvents.Event()
        var currentForeground: String? = null

        while (usageEvents.hasNextEvent()) {
            usageEvents.getNextEvent(event)
            if (event.eventType == UsageEvents.Event.ACTIVITY_RESUMED ||
                event.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND
            ) {
                currentForeground = event.packageName
            }
        }

        return currentForeground
    }
}
