package com.example.model

/**
 * Types of questions supported in CBSE Board examination patterns.
 */
enum class SundayQuestionType {
    MCQ,
    VERY_SHORT,
    SHORT,
    LONG,
    CASE_STUDY
}

/**
 * CBSE Exam Sections with mark distributions.
 */
enum class SundaySection(
    val title: String,
    val description: String,
    val marksPerQuestion: Int
) {
    SECTION_A("Section A", "Multiple Choice Questions", 1),
    SECTION_B("Section B", "Very Short Answer Questions", 2),
    SECTION_C("Section C", "Short Answer Questions", 3),
    SECTION_D("Section D", "Long Answer Questions", 5),
    SECTION_E("Section E", "Case-Based / Source Integrated Questions", 4)
}

/**
 * Represents a single question in the 80-mark CBSE Sunday Test.
 */
data class SundayTestQuestion(
    val id: String,
    val number: Int,
    val section: SundaySection,
    val type: SundayQuestionType,
    val marks: Int,
    val question: String,
    val options: List<String> = emptyList(), // For MCQs
    val correctOptionIndex: Int? = null,    // For MCQs
    val caseStudyContext: String? = null,   // For Case-Based Questions
    val modelAnswer: String,                // Ideal answer / solution
    val rubricKeywords: List<String>,       // Evaluated in Layer 1 Internal AI
    val webVerificationSources: List<String> // Evaluated in Layer 2 Web-Grounded AI
)

/**
 * Complete 80-Mark CBSE Sunday Test Paper.
 */
data class SundayTestPaper(
    val id: String,
    val classId: String,
    val className: String,
    val subjectId: String,
    val subjectName: String,
    val totalMarks: Int = 80,
    val passingMarks: Int = 60,
    val durationSeconds: Int = 3 * 60 * 60, // 3 hours (10,800 seconds)
    val questions: List<SundayTestQuestion>
)

/**
 * User's submitted response for a question.
 */
data class SundayUserResponse(
    val questionId: String,
    val selectedOptionIndex: Int? = null,
    val textAnswer: String = "",
    val isSkipped: Boolean = false
)

/**
 * Evaluation breakdown for an individual question after Layer 1 & Layer 2 verification.
 */
data class QuestionEvaluationResult(
    val questionNumber: Int,
    val questionText: String,
    val section: SundaySection,
    val maxMarks: Int,
    val marksAwarded: Int,
    val userResponseText: String,
    val modelAnswerText: String,
    val layer1Feedback: String,
    val layer2WebGroundedFeedback: String,
    val verifiedSources: List<String>
)

/**
 * Persisted result record for the Sunday Test.
 */
data class SundayTestResult(
    val testId: String,
    val classId: String,
    val className: String,
    val subjectId: String,
    val subjectName: String,
    val totalMarks: Int = 80,
    val score: Int,
    val isPassed: Boolean, // score >= 60
    val percentage: Float,
    val sectionScores: Map<String, Int>, // "Section A" -> 18, etc.
    val sectionMaxMarks: Map<String, Int>,
    val questionResults: List<QuestionEvaluationResult>,
    val timeTakenSeconds: Int,
    val completedAtMillis: Long,
    val overallLayer1Summary: String,
    val overallLayer2Summary: String
)

/**
 * Record for tracking attempts (max 3) and cooldowns (2 hours) per Sunday.
 */
data class SundayAttemptRecord(
    val attemptNumber: Int,
    val timestamp: Long,
    val score: Int,
    val isPassed: Boolean,
    val subjectName: String
)
