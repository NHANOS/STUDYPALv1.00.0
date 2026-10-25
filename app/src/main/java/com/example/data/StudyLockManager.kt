package com.example.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject

/**
 * Represents the active in-progress study session.
 */
data class ActiveStudySession(
    val classId: String,
    val subjectId: String,
    val chapterId: String,
    val chapterTitle: String,
    val totalDurationMinutes: Int,
    val remainingSeconds: Int,
    val isRunning: Boolean,
    val isTimerCompleted: Boolean,
    val blockedPackages: Set<String>,
    val startedAtMillis: Long
)

/**
 * Represents the class-wide lock enforced when starting an incomplete chapter.
 */
data class ActiveClassLock(
    val classId: String,
    val subjectId: String,
    val chapterId: String,
    val chapterTitle: String,
    val lockedAtMillis: Long
)

/**
 * Result record for the 80-mark chapter test.
 */
data class ChapterTestResult(
    val chapterId: String,
    val score: Int, // Out of 80
    val maxScore: Int = 80,
    val isPassed: Boolean, // Score >= 60
    val timestamp: Long
)

/**
 * Manages study session countdown, app blocker target list, completion state,
 * 80-mark test results, and class-wide chapter locking/unlocking.
 */
class StudyLockManager private constructor(context: Context) {

    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences("study_lock_preferences", Context.MODE_PRIVATE)

    private val _activeSession = MutableStateFlow<ActiveStudySession?>(loadActiveSession())
    val activeSession: StateFlow<ActiveStudySession?> = _activeSession.asStateFlow()

    private val _activeClassLocks = MutableStateFlow<Map<String, ActiveClassLock>>(loadClassLocks())
    val activeClassLocks: StateFlow<Map<String, ActiveClassLock>> = _activeClassLocks.asStateFlow()

    private val _completedChapters = MutableStateFlow<Map<String, ChapterTestResult>>(loadCompletedChapters())
    val completedChapters: StateFlow<Map<String, ChapterTestResult>> = _completedChapters.asStateFlow()

    private val _isAlarmRinging = MutableStateFlow(false)
    val isAlarmRinging: StateFlow<Boolean> = _isAlarmRinging.asStateFlow()

    // Flag for blocked app detection popup
    private val _lastBlockedAppAlert = MutableStateFlow<String?>(null)
    val lastBlockedAppAlert: StateFlow<String?> = _lastBlockedAppAlert.asStateFlow()

    companion object {
        @Volatile
        private var INSTANCE: StudyLockManager? = null

        fun getInstance(context: Context): StudyLockManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: StudyLockManager(context.applicationContext).also { INSTANCE = it }
            }
        }

        const val MINIMUM_STUDY_DURATION_MINUTES = 25
        const val PASSING_SCORE_MARKS = 60
        const val TOTAL_TEST_MARKS = 80
    }

    /**
     * Check if a specific chapter is already completed (passed with >= 60 marks).
     */
    fun isChapterCompleted(chapterId: String): Boolean {
        val result = _completedChapters.value[chapterId]
        return result != null && result.isPassed
    }

    /**
     * Get test result for a chapter.
     */
    fun getChapterTestResult(chapterId: String): ChapterTestResult? {
        return _completedChapters.value[chapterId]
    }

    /**
     * Check if a chapter is currently locked.
     * Rule: If an incomplete chapter was started in this class level, ALL other chapters
     * in the class (in the same or different subjects) are locked until the test is passed with 60+ marks.
     * The active chapter itself remains accessible.
     */
    fun isChapterLocked(classId: String, chapterId: String): Boolean {
        val classLock = _activeClassLocks.value[classId] ?: return false
        // If there's an active class lock, any chapter other than the active chapter is locked!
        return classLock.chapterId != chapterId
    }

    /**
     * Get the active class lock for a class, if any.
     */
    fun getClassLock(classId: String): ActiveClassLock? {
        return _activeClassLocks.value[classId]
    }

    /**
     * Start a study timer session.
     * If the chapter is NOT already completed, lock all other chapters in this class level.
     * If chapter is already completed, do NOT re-trigger class lock.
     */
    @Synchronized
    fun startStudySession(
        classId: String,
        subjectId: String,
        chapterId: String,
        chapterTitle: String,
        durationMinutes: Int,
        blockedPackages: Set<String>
    ): ActiveStudySession {
        val duration = durationMinutes.coerceAtLeast(MINIMUM_STUDY_DURATION_MINUTES)
        val isAlreadyCompleted = isChapterCompleted(chapterId)

        // Class-wide lock rule:
        // When a user starts an incomplete chapter, automatically lock all chapters in the current subject
        // and other subjects for that entire Class level.
        // If already completed, do NOT trigger the class-wide lock again.
        if (!isAlreadyCompleted) {
            val lock = ActiveClassLock(
                classId = classId,
                subjectId = subjectId,
                chapterId = chapterId,
                chapterTitle = chapterTitle,
                lockedAtMillis = System.currentTimeMillis()
            )
            val updatedLocks = _activeClassLocks.value.toMutableMap()
            updatedLocks[classId] = lock
            _activeClassLocks.value = updatedLocks
            saveClassLocks(updatedLocks)
        }

        val session = ActiveStudySession(
            classId = classId,
            subjectId = subjectId,
            chapterId = chapterId,
            chapterTitle = chapterTitle,
            totalDurationMinutes = duration,
            remainingSeconds = duration * 60,
            isRunning = true,
            isTimerCompleted = false,
            blockedPackages = blockedPackages,
            startedAtMillis = System.currentTimeMillis()
        )
        _activeSession.value = session
        saveActiveSession(session)
        return session
    }

    /**
     * Update countdown timer from service.
     */
    @Synchronized
    fun updateSessionCountdown(remainingSeconds: Int, isCompleted: Boolean) {
        val current = _activeSession.value ?: return
        val updated = current.copy(
            remainingSeconds = remainingSeconds,
            isRunning = !isCompleted,
            isTimerCompleted = isCompleted
        )
        _activeSession.value = updated
        saveActiveSession(updated)

        if (isCompleted) {
            _isAlarmRinging.value = true
        }
    }

    /**
     * Silence the completion alarm.
     */
    fun stopAlarm() {
        _isAlarmRinging.value = false
    }

    /**
     * Post an alert for a blocked application attempt.
     */
    fun notifyBlockedAppTriggered(appNameOrPackage: String) {
        _lastBlockedAppAlert.value = appNameOrPackage
    }

    fun clearBlockedAppAlert() {
        _lastBlockedAppAlert.value = null
    }

    /**
     * Record 80-mark test score.
     * If score >= 60:
     * - Record chapter completion.
     * - Unlock all chapters for that entire Class level (remove class lock).
     */
    @Synchronized
    fun recordTestScore(
        classId: String,
        subjectId: String,
        chapterId: String,
        score: Int
    ): ChapterTestResult {
        val passed = score >= PASSING_SCORE_MARKS
        val testResult = ChapterTestResult(
            chapterId = chapterId,
            score = score,
            maxScore = TOTAL_TEST_MARKS,
            isPassed = passed,
            timestamp = System.currentTimeMillis()
        )

        val updatedCompleted = _completedChapters.value.toMutableMap()
        updatedCompleted[chapterId] = testResult
        _completedChapters.value = updatedCompleted
        saveCompletedChapters(updatedCompleted)

        if (passed) {
            // Unlock all chapters for that class level!
            val updatedLocks = _activeClassLocks.value.toMutableMap()
            if (updatedLocks.containsKey(classId)) {
                updatedLocks.remove(classId)
                _activeClassLocks.value = updatedLocks
                saveClassLocks(updatedLocks)
            }
        }

        return testResult
    }

    /**
     * Clear the active session once user concludes.
     */
    @Synchronized
    fun clearActiveSession() {
        _activeSession.value = null
        prefs.edit().remove("active_session").apply()
        _isAlarmRinging.value = false
    }

    /**
     * Clear all recorded completed chapters from memory and local SharedPreferences.
     */
    @Synchronized
    fun clearAllCompletedChapters() {
        _completedChapters.value = emptyMap()
        prefs.edit().remove("completed_chapters").apply()
    }

    /**
     * Resets all internal in-memory and shared preferences state for unit testing.
     */
    @Synchronized
    fun resetForTesting() {
        _completedChapters.value = emptyMap()
        _activeSession.value = null
        _activeClassLocks.value = emptyMap()
        _isAlarmRinging.value = false
        prefs.edit().clear().apply()
    }

    // --- Persistence Helpers ---

    private fun saveActiveSession(session: ActiveStudySession?) {
        if (session == null) {
            prefs.edit().remove("active_session").apply()
            return
        }
        val json = JSONObject().apply {
            put("classId", session.classId)
            put("subjectId", session.subjectId)
            put("chapterId", session.chapterId)
            put("chapterTitle", session.chapterTitle)
            put("totalDurationMinutes", session.totalDurationMinutes)
            put("remainingSeconds", session.remainingSeconds)
            put("isRunning", session.isRunning)
            put("isTimerCompleted", session.isTimerCompleted)
            put("startedAtMillis", session.startedAtMillis)
            val pkgsArray = JSONArray()
            session.blockedPackages.forEach { pkgsArray.put(it) }
            put("blockedPackages", pkgsArray)
        }
        prefs.edit().putString("active_session", json.toString()).apply()
    }

    private fun loadActiveSession(): ActiveStudySession? {
        val raw = prefs.getString("active_session", null) ?: return null
        return try {
            val json = JSONObject(raw)
            val pkgs = mutableSetOf<String>()
            val pkgsArray = json.optJSONArray("blockedPackages")
            if (pkgsArray != null) {
                for (i in 0 until pkgsArray.length()) {
                    pkgs.add(pkgsArray.getString(i))
                }
            }
            ActiveStudySession(
                classId = json.getString("classId"),
                subjectId = json.getString("subjectId"),
                chapterId = json.getString("chapterId"),
                chapterTitle = json.getString("chapterTitle"),
                totalDurationMinutes = json.getInt("totalDurationMinutes"),
                remainingSeconds = json.getInt("remainingSeconds"),
                isRunning = json.getBoolean("isRunning"),
                isTimerCompleted = json.getBoolean("isTimerCompleted"),
                blockedPackages = pkgs,
                startedAtMillis = json.getLong("startedAtMillis")
            )
        } catch (_: Exception) {
            null
        }
    }

    private fun saveClassLocks(locks: Map<String, ActiveClassLock>) {
        val jsonArray = JSONArray()
        for (lock in locks.values) {
            val obj = JSONObject().apply {
                put("classId", lock.classId)
                put("subjectId", lock.subjectId)
                put("chapterId", lock.chapterId)
                put("chapterTitle", lock.chapterTitle)
                put("lockedAtMillis", lock.lockedAtMillis)
            }
            jsonArray.put(obj)
        }
        prefs.edit().putString("class_locks", jsonArray.toString()).apply()
    }

    private fun loadClassLocks(): Map<String, ActiveClassLock> {
        val raw = prefs.getString("class_locks", null) ?: return emptyMap()
        val result = mutableMapOf<String, ActiveClassLock>()
        try {
            val jsonArray = JSONArray(raw)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val lock = ActiveClassLock(
                    classId = obj.getString("classId"),
                    subjectId = obj.getString("subjectId"),
                    chapterId = obj.getString("chapterId"),
                    chapterTitle = obj.getString("chapterTitle"),
                    lockedAtMillis = obj.getLong("lockedAtMillis")
                )
                result[lock.classId] = lock
            }
        } catch (_: Exception) {
            // Ignore corrupted preferences
        }
        return result
    }

    private fun saveCompletedChapters(completed: Map<String, ChapterTestResult>) {
        val jsonArray = JSONArray()
        for (res in completed.values) {
            val obj = JSONObject().apply {
                put("chapterId", res.chapterId)
                put("score", res.score)
                put("maxScore", res.maxScore)
                put("isPassed", res.isPassed)
                put("timestamp", res.timestamp)
            }
            jsonArray.put(obj)
        }
        prefs.edit().putString("completed_chapters", jsonArray.toString()).apply()
    }

    private fun loadCompletedChapters(): Map<String, ChapterTestResult> {
        val raw = prefs.getString("completed_chapters", null) ?: return emptyMap()
        val result = mutableMapOf<String, ChapterTestResult>()
        try {
            val jsonArray = JSONArray(raw)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val res = ChapterTestResult(
                    chapterId = obj.getString("chapterId"),
                    score = obj.getInt("score"),
                    maxScore = obj.optInt("maxScore", 80),
                    isPassed = obj.getBoolean("isPassed"),
                    timestamp = obj.getLong("timestamp")
                )
                result[res.chapterId] = res
            }
        } catch (_: Exception) {
            // Ignore corrupted preferences
        }
        return result
    }
}
