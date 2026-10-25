package com.example.data

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

data class UserProfile(
    val name: String,
    val imagePath: String?
) {
    val displayInitial: String
        get() {
            val trimmed = name.trim()
            return if (trimmed.isNotEmpty()) {
                trimmed.first().uppercaseChar().toString()
            } else {
                "S"
            }
        }
}

class ProfileManager private constructor(private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _profileState = MutableStateFlow(readProfileFromDisk())
    val profileState: StateFlow<UserProfile> = _profileState.asStateFlow()

    private fun readProfileFromDisk(): UserProfile {
        val name = prefs.getString(KEY_NAME, DEFAULT_NAME) ?: DEFAULT_NAME
        val path = prefs.getString(KEY_IMAGE_PATH, null)
        val validPath = if (path != null && File(path).exists()) path else null
        return UserProfile(name = name, imagePath = validPath)
    }

    fun getProfile(): UserProfile = _profileState.value

    fun saveName(newName: String): Boolean {
        val trimmed = newName.trim()
        if (trimmed.isEmpty()) return false
        prefs.edit().putString(KEY_NAME, trimmed).apply()
        _profileState.value = _profileState.value.copy(name = trimmed)
        return true
    }

    fun saveProfileImageFromUri(uri: Uri): String? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            if (inputStream != null) {
                val destinationFile = File(context.filesDir, AVATAR_FILE_NAME)
                FileOutputStream(destinationFile).use { output ->
                    inputStream.copyTo(output)
                }
                inputStream.close()
                val absolutePath = destinationFile.absolutePath
                prefs.edit().putString(KEY_IMAGE_PATH, absolutePath).apply()
                _profileState.value = _profileState.value.copy(imagePath = absolutePath)
                absolutePath
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun clearProfileImage() {
        val destinationFile = File(context.filesDir, AVATAR_FILE_NAME)
        if (destinationFile.exists()) {
            destinationFile.delete()
        }
        prefs.edit().remove(KEY_IMAGE_PATH).apply()
        _profileState.value = _profileState.value.copy(imagePath = null)
    }

    fun recordStudyProgress(hours: Float, chapterPassed: Boolean) {
        val currentHours = prefs.getFloat("metric_study_hours", 0f)
        val currentTestsPassed = prefs.getInt("metric_tests_passed", 0)
        prefs.edit()
            .putFloat("metric_study_hours", currentHours + hours)
            .putInt("metric_tests_passed", currentTestsPassed + if (chapterPassed) 1 else 0)
            .apply()
    }

    fun getMetricStudyHours(): Float = prefs.getFloat("metric_study_hours", 0f)
    fun getMetricTestsPassed(): Int = prefs.getInt("metric_tests_passed", 0)

    companion object {
        private const val PREFS_NAME = "study_x_profile_prefs"
        private const val KEY_NAME = "profile_display_name"
        private const val KEY_IMAGE_PATH = "profile_image_path"
        private const val AVATAR_FILE_NAME = "study_x_avatar.jpg"
        const val DEFAULT_NAME = "Student"

        @Volatile
        private var INSTANCE: ProfileManager? = null

        fun getInstance(context: Context): ProfileManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ProfileManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}
