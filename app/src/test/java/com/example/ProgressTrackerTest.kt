package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.ChapterTestResult
import com.example.data.StudyLockManager
import com.example.model.StudyRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class ProgressTrackerTest {

    private lateinit var context: Context
    private lateinit var studyLockManager: StudyLockManager

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        studyLockManager = StudyLockManager.getInstance(context)
        studyLockManager.resetForTesting()
    }

    @Test
    fun testStrictPassedDataOnlyFiltering() {
        // Record a failed test (< 60 marks, e.g. 45/80)
        val failedResult = studyLockManager.recordTestScore(
            classId = "class_10",
            subjectId = "math_10",
            chapterId = "c10_m_ch1",
            score = 45
        )
        assertFalse("Score 45 must NOT be passed", failedResult.isPassed)
        assertFalse("Chapter must NOT be marked completed when failed", studyLockManager.isChapterCompleted("c10_m_ch1"))

        // Record a passed test (>= 60 marks, e.g. 68/80)
        val passedResult = studyLockManager.recordTestScore(
            classId = "class_10",
            subjectId = "math_10",
            chapterId = "c10_m_ch2",
            score = 68
        )
        assertTrue("Score 68 must be marked as passed", passedResult.isPassed)
        assertTrue("Chapter must be completed when score >= 60", studyLockManager.isChapterCompleted("c10_m_ch2"))

        // Verify strictly passed records
        val allStored = studyLockManager.completedChapters.value
        val passedOnly = allStored.values.filter { it.isPassed && it.score >= 60 }

        assertEquals(1, passedOnly.size)
        assertEquals("c10_m_ch2", passedOnly.first().chapterId)
        assertEquals(68, passedOnly.first().score)
    }

    @Test
    fun testClassSyllabusProgressCalculations() {
        val class10 = StudyRepository.getClass("class_10")
        assertNotNull("Class 10 must exist", class10)

        val totalChapters = StudyRepository.getTotalChaptersCount("class_10")
        assertTrue("Class 10 has chapters", totalChapters > 0)

        // Initially 0 passed
        val initialPassed = studyLockManager.completedChapters.value.values.filter { it.isPassed && it.score >= 60 }.size
        assertEquals(0, initialPassed)

        val initialRemaining = totalChapters - initialPassed
        assertEquals(totalChapters, initialRemaining)

        // Pass 2 chapters with 60+ marks
        studyLockManager.recordTestScore("class_10", "math_10", "c10_m_ch1", 72)
        studyLockManager.recordTestScore("class_10", "math_10", "c10_m_ch2", 64)

        val updatedPassed = studyLockManager.completedChapters.value.values.filter { it.isPassed && it.score >= 60 }.size
        assertEquals(2, updatedPassed)

        val updatedRemaining = totalChapters - updatedPassed
        assertEquals(totalChapters - 2, updatedRemaining)

        val passedPercentage = (updatedPassed.toFloat() / totalChapters.toFloat()) * 100f
        assertTrue("Percentage is calculated correctly", passedPercentage > 0f && passedPercentage <= 100f)
    }

    @Test
    fun testFindChapterLocationMapping() {
        val loc = StudyRepository.findChapter("c10_m_ch1")
        assertNotNull("c10_m_ch1 must be located in curriculum", loc)
        val (classInfo, subjectInfo, chapterInfo) = loc!!
        assertEquals("class_10", classInfo.id)
        assertEquals("Real Numbers", chapterInfo.title)
        assertEquals(1, chapterInfo.number)
    }
}
