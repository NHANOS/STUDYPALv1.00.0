package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.SundayTestManager
import com.example.model.SundayQuestionType
import com.example.model.SundaySection
import com.example.model.SundayUserResponse
import com.example.service.SundayTestEvaluator
import com.example.service.SundayTestGenerator
import kotlinx.coroutines.runBlocking
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
class SundayTestFeatureTest {

    private lateinit var context: Context
    private lateinit var sundayManager: SundayTestManager

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        sundayManager = SundayTestManager.getInstance(context)
        // Ensure simulation mode is enabled for deterministic testing
        sundayManager.setSimulationMode(true)
        sundayManager.bypassCooldownForTesting()
    }

    @Test
    fun testSundayRestrictionMessage() {
        assertEquals(
            "This test is only available on Sunday. You cannot take the test today.",
            SundayTestManager.RESTRICTION_MESSAGE
        )
    }

    @Test
    fun testCBSE80MarkPaperGenerationStructure() {
        val mathPaper = SundayTestGenerator.generatePaper(
            classId = "class_10",
            className = "Class 10",
            subjectId = "math_10",
            subjectName = "Mathematics"
        )

        // Must strictly total 80 marks
        assertEquals(80, mathPaper.totalMarks)
        assertEquals(60, mathPaper.passingMarks)
        assertEquals(10800, mathPaper.durationSeconds) // 3 hours

        // Check Section A: 20 MCQs (1 mark each = 20)
        val secA = mathPaper.questions.filter { it.section == SundaySection.SECTION_A }
        assertEquals(20, secA.size)
        secA.forEach {
            assertEquals(1, it.marks)
            assertEquals(SundayQuestionType.MCQ, it.type)
            assertEquals(4, it.options.size)
            assertNotNull(it.correctOptionIndex)
        }

        // Check Section B: 5 Very Short Answer (2 marks each = 10)
        val secB = mathPaper.questions.filter { it.section == SundaySection.SECTION_B }
        assertEquals(5, secB.size)
        secB.forEach {
            assertEquals(2, it.marks)
            assertEquals(SundayQuestionType.VERY_SHORT, it.type)
        }

        // Check Section C: 6 Short Answer (3 marks each = 18)
        val secC = mathPaper.questions.filter { it.section == SundaySection.SECTION_C }
        assertEquals(6, secC.size)
        secC.forEach {
            assertEquals(3, it.marks)
            assertEquals(SundayQuestionType.SHORT, it.type)
        }

        // Check Section D: 4 Long Answer (5 marks each = 20)
        val secD = mathPaper.questions.filter { it.section == SundaySection.SECTION_D }
        assertEquals(4, secD.size)
        secD.forEach {
            assertEquals(5, it.marks)
            assertEquals(SundayQuestionType.LONG, it.type)
        }

        // Check Section E: 3 Case Study Questions (4 marks each = 12)
        val secE = mathPaper.questions.filter { it.section == SundaySection.SECTION_E }
        assertEquals(3, secE.size)
        secE.forEach {
            assertEquals(4, it.marks)
            assertEquals(SundayQuestionType.CASE_STUDY, it.type)
            assertNotNull(it.caseStudyContext)
        }

        // Total marks sum check: 20 + 10 + 18 + 20 + 12 = 80
        val sumMarks = mathPaper.questions.sumOf { it.marks }
        assertEquals(80, sumMarks)
    }

    @Test
    fun testAttemptsAndCooldownLogic() {
        val initialRemaining = sundayManager.getRemainingAttempts()
        assertTrue(initialRemaining in 1..3)

        // Record an attempt
        sundayManager.recordAttempt(score = 70, isPassed = true, subjectName = "Mathematics")

        // 2-hour cooldown must now be active
        assertTrue(sundayManager.isCooldownActive())
        assertTrue(sundayManager.getCooldownRemainingMillis() > 0)

        // Attempting to start now should be blocked by cooldown
        val (canStart, reason) = sundayManager.canStartTest()
        assertFalse(canStart)
        assertTrue(reason?.contains("Cooldown", ignoreCase = true) == true)

        // Bypass cooldown (e.g. for developer / simulation)
        sundayManager.bypassCooldownForTesting()
        assertFalse(sundayManager.isCooldownActive())
    }

    @Test
    fun testTwoLayerEvaluationPipelineAndPassingBenchmark() = runBlocking {
        val paper = SundayTestGenerator.generatePaper(
            classId = "class_10",
            className = "Class 10",
            subjectId = "science_10",
            subjectName = "Science"
        )

        val responses = mutableMapOf<String, SundayUserResponse>()
        // Provide correct answers for MCQs
        paper.questions.forEach { q ->
            if (q.type == SundayQuestionType.MCQ) {
                responses[q.id] = SundayUserResponse(
                    questionId = q.id,
                    selectedOptionIndex = q.correctOptionIndex,
                    isSkipped = false
                )
            } else {
                // Provide substantive responses for subjective questions
                responses[q.id] = SundayUserResponse(
                    questionId = q.id,
                    textAnswer = "The key scientific principle, definition, theorem formula and verified derivation: " +
                            q.rubricKeywords.joinToString(" ") + " and comprehensive stepwise explanation.",
                    isSkipped = false
                )
            }
        }

        val evaluatedResult = SundayTestEvaluator.evaluatePaper(
            paper = paper,
            userResponses = responses,
            timeTakenSeconds = 3600,
            onProgressUpdate = { _, _ -> }
        )

        assertEquals(80, evaluatedResult.totalMarks)
        assertTrue("Score should be >= 60 for passing benchmark", evaluatedResult.score >= 60)
        assertTrue(evaluatedResult.isPassed)
        assertNotNull(evaluatedResult.overallLayer1Summary)
        assertNotNull(evaluatedResult.overallLayer2Summary)
        assertEquals(paper.questions.size, evaluatedResult.questionResults.size)
    }
}
