package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.QuestionEvaluationResult
import com.example.model.SundayAttemptRecord
import com.example.model.SundaySection
import com.example.model.SundayTestResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.util.Calendar
import java.util.TimeZone

/**
 * Manages the Sunday availability checks, countdowns, attempts limit (max 3),
 * 2-hour mandatory cooldown, and persistent storage of Sunday Test results.
 */
class SundayTestManager private constructor(context: Context) {

    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences("sunday_test_preferences", Context.MODE_PRIVATE)

    // Simulation toggle to allow testing the Sunday Exam flow anytime
    private val _isSimulationMode = MutableStateFlow(prefs.getBoolean(KEY_SIMULATION_MODE, false))
    val isSimulationMode: StateFlow<Boolean> = _isSimulationMode.asStateFlow()

    private val _attempts = MutableStateFlow<List<SundayAttemptRecord>>(loadAttemptsFromPrefs())
    val attempts: StateFlow<List<SundayAttemptRecord>> = _attempts.asStateFlow()

    private val _savedResults = MutableStateFlow<List<SundayTestResult>>(loadResultsFromPrefs())
    val savedResults: StateFlow<List<SundayTestResult>> = _savedResults.asStateFlow()

    companion object {
        const val MAX_ATTEMPTS_PER_SUNDAY = 3
        const val COOLDOWN_DURATION_MILLIS = 2 * 60 * 60 * 1000L // 2 Hours
        const val PASSING_BENCHMARK = 60 // 60+ marks out of 80 to pass
        const val RESTRICTION_MESSAGE = "This test is only available on Sunday. You cannot take the test today."

        private const val KEY_SIMULATION_MODE = "sunday_simulation_mode"
        private const val KEY_ATTEMPTS = "sunday_attempts_json"
        private const val KEY_RESULTS = "sunday_results_json"
        private const val KEY_LAST_ATTEMPT_TIME = "sunday_last_attempt_time"
        private const val KEY_LAST_RECORDED_SUNDAY_ID = "sunday_last_recorded_date_id"

        @Volatile
        private var INSTANCE: SundayTestManager? = null

        fun getInstance(context: Context): SundayTestManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SundayTestManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    /**
     * Check if the device's current system date is Sunday.
     */
    fun isTodaySunday(): Boolean {
        if (_isSimulationMode.value) return true
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
    }

    /**
     * Set simulation mode on or off.
     */
    fun setSimulationMode(enabled: Boolean) {
        _isSimulationMode.value = enabled
        prefs.edit().putBoolean(KEY_SIMULATION_MODE, enabled).apply()
    }

    /**
     * Calculates the time in milliseconds remaining until upcoming Sunday 00:00:00.
     */
    fun millisUntilNextSunday(): Long {
        val now = Calendar.getInstance()
        val nextSunday = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val currentDay = now.get(Calendar.DAY_OF_WEEK)
        val daysToAdd = if (currentDay == Calendar.SUNDAY) {
            // If it's already Sunday, this would be for the next Sunday
            7
        } else {
            (Calendar.SUNDAY - currentDay + 7) % 7
        }

        nextSunday.add(Calendar.DAY_OF_YEAR, daysToAdd)
        val diff = nextSunday.timeInMillis - now.timeInMillis
        return diff.coerceAtLeast(0L)
    }

    /**
     * Formats milliseconds into Days, Hours, Minutes, Seconds.
     */
    fun formatCountdown(millis: Long): String {
        val totalSeconds = (millis / 1000).coerceAtLeast(0)
        val days = totalSeconds / 86400
        val hours = (totalSeconds % 86400) / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        return String.format("%02dd : %02dh : %02dm : %02ds", days, hours, minutes, seconds)
    }

    /**
     * Formats remaining cooldown time into HH:MM:SS.
     */
    fun formatCooldownTime(millis: Long): String {
        val totalSeconds = (millis / 1000).coerceAtLeast(0)
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    /**
     * Returns true if 2-hour rest cooldown is currently active.
     */
    fun isCooldownActive(): Boolean {
        val lastAttemptTime = prefs.getLong(KEY_LAST_ATTEMPT_TIME, 0L)
        if (lastAttemptTime <= 0L) return false
        val elapsed = System.currentTimeMillis() - lastAttemptTime
        return elapsed < COOLDOWN_DURATION_MILLIS
    }

    /**
     * Returns remaining milliseconds of cooldown.
     */
    fun getCooldownRemainingMillis(): Long {
        val lastAttemptTime = prefs.getLong(KEY_LAST_ATTEMPT_TIME, 0L)
        if (lastAttemptTime <= 0L) return 0L
        val elapsed = System.currentTimeMillis() - lastAttemptTime
        return (COOLDOWN_DURATION_MILLIS - elapsed).coerceAtLeast(0L)
    }

    /**
     * Check if user has attempts remaining today (Max 3).
     */
    fun getRemainingAttempts(): Int {
        checkAndResetIfNewSunday()
        val used = _attempts.value.size
        return (MAX_ATTEMPTS_PER_SUNDAY - used).coerceAtLeast(0)
    }

    /**
     * Check whether a new test can be started right now.
     */
    fun canStartTest(): Pair<Boolean, String?> {
        if (!isTodaySunday()) {
            return Pair(false, RESTRICTION_MESSAGE)
        }
        checkAndResetIfNewSunday()
        if (_attempts.value.size >= MAX_ATTEMPTS_PER_SUNDAY) {
            return Pair(false, "You have reached the maximum limit of 3 test attempts for this Sunday.")
        }
        if (isCooldownActive()) {
            val remaining = formatCooldownTime(getCooldownRemainingMillis())
            return Pair(false, "Mandatory 2-Hour Rest Cooldown in effect. Time remaining: $remaining")
        }
        return Pair(true, null)
    }

    /**
     * Records a test attempt and marks cooldown.
     */
    @Synchronized
    fun recordAttempt(score: Int, isPassed: Boolean, subjectName: String) {
        checkAndResetIfNewSunday()
        val currentList = _attempts.value.toMutableList()
        val newAttempt = SundayAttemptRecord(
            attemptNumber = currentList.size + 1,
            timestamp = System.currentTimeMillis(),
            score = score,
            isPassed = isPassed,
            subjectName = subjectName
        )
        currentList.add(newAttempt)
        _attempts.value = currentList
        saveAttemptsToPrefs(currentList)

        // Set cooldown timestamp
        prefs.edit().putLong(KEY_LAST_ATTEMPT_TIME, System.currentTimeMillis()).apply()
    }

    /**
     * Resets the cooldown timer manually (used for developer simulation).
     */
    fun bypassCooldownForTesting() {
        prefs.edit().putLong(KEY_LAST_ATTEMPT_TIME, 0L).apply()
    }

    /**
     * Resets test attempts (used for testing and evaluation).
     */
    fun resetAttemptsForTesting() {
        _attempts.value = emptyList()
        prefs.edit().remove(KEY_ATTEMPTS).remove(KEY_LAST_ATTEMPT_TIME).apply()
    }

    /**
     * Saves full 80-mark test results with Layer 1 & Layer 2 breakdown.
     */
    @Synchronized
    fun saveTestResult(result: SundayTestResult) {
        val currentList = _savedResults.value.toMutableList()
        currentList.add(0, result) // Add to top
        _savedResults.value = currentList
        saveResultsToPrefs(currentList)
    }

    // --- Internal Persistence ---

    private fun getCurrentSundayIdentifier(): String {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        val year = calendar.get(Calendar.YEAR)
        val week = calendar.get(Calendar.WEEK_OF_YEAR)
        return "$year-$week"
    }

    private fun checkAndResetIfNewSunday() {
        val currentId = getCurrentSundayIdentifier()
        val lastId = prefs.getString(KEY_LAST_RECORDED_SUNDAY_ID, null)
        if (lastId != currentId) {
            // Reset attempts for new Sunday
            _attempts.value = emptyList()
            prefs.edit()
                .putString(KEY_LAST_RECORDED_SUNDAY_ID, currentId)
                .remove(KEY_ATTEMPTS)
                .remove(KEY_LAST_ATTEMPT_TIME)
                .apply()
        }
    }

    private fun saveAttemptsToPrefs(attempts: List<SundayAttemptRecord>) {
        val jsonArray = JSONArray()
        for (att in attempts) {
            val obj = JSONObject().apply {
                put("attemptNumber", att.attemptNumber)
                put("timestamp", att.timestamp)
                put("score", att.score)
                put("isPassed", att.isPassed)
                put("subjectName", att.subjectName)
            }
            jsonArray.put(obj)
        }
        prefs.edit().putString(KEY_ATTEMPTS, jsonArray.toString()).apply()
    }

    private fun loadAttemptsFromPrefs(): List<SundayAttemptRecord> {
        val raw = prefs.getString(KEY_ATTEMPTS, null) ?: return emptyList()
        val list = mutableListOf<SundayAttemptRecord>()
        try {
            val jsonArray = JSONArray(raw)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                list.add(
                    SundayAttemptRecord(
                        attemptNumber = obj.getInt("attemptNumber"),
                        timestamp = obj.getLong("timestamp"),
                        score = obj.getInt("score"),
                        isPassed = obj.getBoolean("isPassed"),
                        subjectName = obj.optString("subjectName", "CBSE Subject")
                    )
                )
            }
        } catch (_: Exception) {}
        return list
    }

    private fun saveResultsToPrefs(results: List<SundayTestResult>) {
        val jsonArray = JSONArray()
        for (res in results) {
            val obj = JSONObject().apply {
                put("testId", res.testId)
                put("classId", res.classId)
                put("className", res.className)
                put("subjectId", res.subjectId)
                put("subjectName", res.subjectName)
                put("totalMarks", res.totalMarks)
                put("score", res.score)
                put("isPassed", res.isPassed)
                put("percentage", res.percentage.toDouble())
                put("timeTakenSeconds", res.timeTakenSeconds)
                put("completedAtMillis", res.completedAtMillis)
                put("overallLayer1Summary", res.overallLayer1Summary)
                put("overallLayer2Summary", res.overallLayer2Summary)

                // Section scores
                val secObj = JSONObject()
                res.sectionScores.forEach { (k, v) -> secObj.put(k, v) }
                put("sectionScores", secObj)

                val secMaxObj = JSONObject()
                res.sectionMaxMarks.forEach { (k, v) -> secMaxObj.put(k, v) }
                put("sectionMaxMarks", secMaxObj)

                // Question Results (up to 40 questions)
                val qArray = JSONArray()
                for (q in res.questionResults) {
                    val qObj = JSONObject().apply {
                        put("questionNumber", q.questionNumber)
                        put("questionText", q.questionText)
                        put("sectionName", q.section.name)
                        put("maxMarks", q.maxMarks)
                        put("marksAwarded", q.marksAwarded)
                        put("userResponseText", q.userResponseText)
                        put("modelAnswerText", q.modelAnswerText)
                        put("layer1Feedback", q.layer1Feedback)
                        put("layer2WebGroundedFeedback", q.layer2WebGroundedFeedback)
                        val srcArray = JSONArray()
                        q.verifiedSources.forEach { srcArray.put(it) }
                        put("verifiedSources", srcArray)
                    }
                    qArray.put(qObj)
                }
                put("questionResults", qArray)
            }
            jsonArray.put(obj)
        }
        prefs.edit().putString(KEY_RESULTS, jsonArray.toString()).apply()
    }

    private fun loadResultsFromPrefs(): List<SundayTestResult> {
        val raw = prefs.getString(KEY_RESULTS, null) ?: return emptyList()
        val list = mutableListOf<SundayTestResult>()
        try {
            val jsonArray = JSONArray(raw)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val secObj = obj.optJSONObject("sectionScores")
                val sectionScores = mutableMapOf<String, Int>()
                secObj?.keys()?.forEach { k -> sectionScores[k] = secObj.getInt(k) }

                val secMaxObj = obj.optJSONObject("sectionMaxMarks")
                val sectionMaxMarks = mutableMapOf<String, Int>()
                secMaxObj?.keys()?.forEach { k -> sectionMaxMarks[k] = secMaxObj.getInt(k) }

                val qArray = obj.optJSONArray("questionResults")
                val qResults = mutableListOf<QuestionEvaluationResult>()
                if (qArray != null) {
                    for (j in 0 until qArray.length()) {
                        val qObj = qArray.getJSONObject(j)
                        val sources = mutableListOf<String>()
                        val srcArray = qObj.optJSONArray("verifiedSources")
                        if (srcArray != null) {
                            for (s in 0 until srcArray.length()) {
                                sources.add(srcArray.getString(s))
                            }
                        }
                        val sec = try {
                            SundaySection.valueOf(qObj.getString("sectionName"))
                        } catch (_: Exception) {
                            SundaySection.SECTION_A
                        }
                        qResults.add(
                            QuestionEvaluationResult(
                                questionNumber = qObj.getInt("questionNumber"),
                                questionText = qObj.getString("questionText"),
                                section = sec,
                                maxMarks = qObj.getInt("maxMarks"),
                                marksAwarded = qObj.getInt("marksAwarded"),
                                userResponseText = qObj.getString("userResponseText"),
                                modelAnswerText = qObj.getString("modelAnswerText"),
                                layer1Feedback = qObj.getString("layer1Feedback"),
                                layer2WebGroundedFeedback = qObj.getString("layer2WebGroundedFeedback"),
                                verifiedSources = sources
                            )
                        )
                    }
                }

                list.add(
                    SundayTestResult(
                        testId = obj.getString("testId"),
                        classId = obj.getString("classId"),
                        className = obj.getString("className"),
                        subjectId = obj.getString("subjectId"),
                        subjectName = obj.getString("subjectName"),
                        totalMarks = obj.optInt("totalMarks", 80),
                        score = obj.getInt("score"),
                        isPassed = obj.getBoolean("isPassed"),
                        percentage = obj.getDouble("percentage").toFloat(),
                        sectionScores = sectionScores,
                        sectionMaxMarks = sectionMaxMarks,
                        questionResults = qResults,
                        timeTakenSeconds = obj.optInt("timeTakenSeconds", 0),
                        completedAtMillis = obj.getLong("completedAtMillis"),
                        overallLayer1Summary = obj.optString("overallLayer1Summary", ""),
                        overallLayer2Summary = obj.optString("overallLayer2Summary", "")
                    )
                )
            }
        } catch (_: Exception) {}
        return list
    }
}
