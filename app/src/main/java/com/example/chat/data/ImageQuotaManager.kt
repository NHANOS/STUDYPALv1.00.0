package com.example.chat.data

import android.content.Context
import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ImageQuotaManager(private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        const val PREFS_NAME = "study_x_ai_quota_prefs"
        const val KEY_LAST_IMAGE_GEN_DATE = "last_image_gen_date"
        const val KEY_DAILY_IMAGE_COUNT = "daily_image_count"
        const val MAX_DAILY_IMAGES = 2
        const val QUOTA_EXCEEDED_MESSAGE =
            "You have reached your daily limit of 2 generated images. Please try again tomorrow."

        fun getTodayDateString(): String {
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            return formatter.format(Date())
        }

        @Volatile
        private var INSTANCE: ImageQuotaManager? = null

        fun getInstance(context: Context): ImageQuotaManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ImageQuotaManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    @Synchronized
    private fun checkAndResetIfNewDay(todayDate: String) {
        val lastDate = prefs.getString(KEY_LAST_IMAGE_GEN_DATE, null)
        if (lastDate != todayDate) {
            prefs.edit()
                .putString(KEY_LAST_IMAGE_GEN_DATE, todayDate)
                .putInt(KEY_DAILY_IMAGE_COUNT, 0)
                .apply()
        }
    }

    @Synchronized
    fun getUsedCount(todayDate: String = getTodayDateString()): Int {
        checkAndResetIfNewDay(todayDate)
        return prefs.getInt(KEY_DAILY_IMAGE_COUNT, 0)
    }

    @Synchronized
    fun getRemainingCount(todayDate: String = getTodayDateString()): Int {
        val used = getUsedCount(todayDate)
        return (MAX_DAILY_IMAGES - used).coerceAtLeast(0)
    }

    @Synchronized
    fun canGenerateImage(todayDate: String = getTodayDateString()): Boolean {
        return getRemainingCount(todayDate) > 0
    }

    @Synchronized
    fun recordImageGenerated(todayDate: String = getTodayDateString()): Boolean {
        checkAndResetIfNewDay(todayDate)
        val current = prefs.getInt(KEY_DAILY_IMAGE_COUNT, 0)
        if (current >= MAX_DAILY_IMAGES) {
            return false
        }
        prefs.edit()
            .putString(KEY_LAST_IMAGE_GEN_DATE, todayDate)
            .putInt(KEY_DAILY_IMAGE_COUNT, current + 1)
            .apply()
        return true
    }

    @Synchronized
    fun resetForTesting(date: String = getTodayDateString(), count: Int = 0) {
        prefs.edit()
            .putString(KEY_LAST_IMAGE_GEN_DATE, date)
            .putInt(KEY_DAILY_IMAGE_COUNT, count)
            .apply()
    }
}
