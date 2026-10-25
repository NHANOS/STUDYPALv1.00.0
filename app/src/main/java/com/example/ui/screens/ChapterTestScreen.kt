package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.LockOpen
import androidx.compose.material.icons.rounded.Quiz
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ProfileManager
import com.example.data.StudyLockManager
import com.example.model.ChapterInfo
import com.example.model.ChapterTestBank
import com.example.model.ClassInfo
import com.example.model.SubjectInfo
import com.example.model.TestQuestion
import com.example.ui.components.AnimeGlowCyan
import com.example.ui.components.AnimeGlowGold
import com.example.ui.components.AnimeGlowPink
import com.example.ui.components.GlassCard

@Composable
fun ChapterTestScreen(
    classInfo: ClassInfo,
    subjectInfo: SubjectInfo,
    chapterInfo: ChapterInfo,
    onBackClick: () -> Unit,
    onTestPassedAndUnlocked: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val context = LocalContext.current
    val studyLockManager = remember { StudyLockManager.getInstance(context) }
    val profileManager = remember { ProfileManager.getInstance(context) }

    val questions = remember {
        ChapterTestBank.generateTestForChapter(classInfo, subjectInfo, chapterInfo)
    }

    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    val userAnswers = remember { mutableStateMapOf<Int, Int>() } // questionIndex -> selectedOptionIndex
    var isSubmitted by remember { mutableStateOf(false) }
    var calculatedScore by remember { mutableIntStateOf(0) }
    var isPassed by remember { mutableStateOf(false) }
    var showSubmitConfirmation by remember { mutableStateOf(false) }
    var showBackExitConfirmation by remember { mutableStateOf(false) }

    BackHandler(enabled = !isSubmitted) {
        showBackExitConfirmation = true
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 680.dp),
            contentPadding = PaddingValues(
                top = contentPadding.calculateTopPadding() + 10.dp,
                bottom = contentPadding.calculateBottomPadding() + 28.dp,
                start = 16.dp,
                end = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Banner
            item {
                TestHeaderBanner(
                    classInfo = classInfo,
                    subjectInfo = subjectInfo,
                    chapterInfo = chapterInfo,
                    isSubmitted = isSubmitted,
                    onBackClick = {
                        if (!isSubmitted) {
                            showBackExitConfirmation = true
                        } else {
                            onBackClick()
                        }
                    }
                )
            }

            if (!isSubmitted) {
                // Question Index Navigator Bar (1 to 20)
                item {
                    QuestionNavigatorRow(
                        totalQuestions = questions.size,
                        currentIndex = currentQuestionIndex,
                        userAnswers = userAnswers,
                        onSelectIndex = { currentQuestionIndex = it }
                    )
                }

                // Active Question Card
                item {
                    val currentQuestion = questions[currentQuestionIndex]
                    ActiveQuestionCard(
                        question = currentQuestion,
                        questionIndex = currentQuestionIndex,
                        totalQuestions = questions.size,
                        selectedOption = userAnswers[currentQuestionIndex],
                        onOptionSelected = { optionIndex ->
                            userAnswers[currentQuestionIndex] = optionIndex
                        }
                    )
                }

                // Previous, Next & Submit controls
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Previous button
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = if (currentQuestionIndex > 0) 0.14f else 0.05f))
                                .clickable(enabled = currentQuestionIndex > 0) {
                                    currentQuestionIndex--
                                }
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                        ) {
                            Text(
                                text = "Previous",
                                color = if (currentQuestionIndex > 0) Color.White else Color.White.copy(alpha = 0.3f),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Next or Submit button
                        if (currentQuestionIndex < questions.size - 1) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Brush.horizontalGradient(listOf(AnimeGlowPink, AnimeGlowCyan)))
                                    .clickable {
                                        currentQuestionIndex++
                                    }
                                    .padding(horizontal = 20.dp, vertical = 12.dp)
                            ) {
                                Text(
                                    text = "Next Question",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Brush.horizontalGradient(listOf(AnimeGlowGold, AnimeGlowPink)))
                                    .clickable {
                                        showSubmitConfirmation = true
                                    }
                                    .padding(horizontal = 24.dp, vertical = 12.dp)
                                    .testTag("submit_test_btn")
                            ) {
                                Text(
                                    text = "Submit Test (80 Marks)",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }
                }
            } else {
                // Post-Submission Result View
                item {
                    TestResultBanner(
                        score = calculatedScore,
                        isPassed = isPassed,
                        totalQuestions = questions.size,
                        answeredCount = userAnswers.size,
                        classInfo = classInfo,
                        onBackToChapter = onBackClick,
                        onUnlockedConfirmed = onTestPassedAndUnlocked,
                        onRetake = {
                            userAnswers.clear()
                            currentQuestionIndex = 0
                            isSubmitted = false
                        }
                    )
                }

                // Review of questions
                item {
                    Text(
                        text = "Detailed Test Review",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                itemsIndexed(questions) { idx, q ->
                    QuestionReviewCard(
                        index = idx + 1,
                        question = q,
                        userAnswer = userAnswers[idx]
                    )
                }
            }
        }
    }

    // Submit Confirmation Dialog
    if (showSubmitConfirmation) {
        val unanswered = questions.size - userAnswers.size
        AlertDialog(
            onDismissRequest = { showSubmitConfirmation = false },
            title = {
                Text("Submit 80-Mark Chapter Test?", color = Color.White, fontWeight = FontWeight.Bold)
            },
            text = {
                Column {
                    Text(
                        text = "You have answered ${userAnswers.size} of ${questions.size} questions.",
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    if (unanswered > 0) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Warning: $unanswered question(s) remain unanswered and will be scored 0 marks.",
                            color = Color(0xFFFF6B6B),
                            fontSize = 13.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Passing Requirement: 60+ marks out of 80 to unlock class chapters.",
                        color = AnimeGlowGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSubmitConfirmation = false
                        // Calculate score: 4 marks per correct answer
                        var score = 0
                        questions.forEachIndexed { i, q ->
                            if (userAnswers[i] == q.correctIndex) {
                                score += ChapterTestBank.MARKS_PER_QUESTION
                            }
                        }
                        calculatedScore = score
                        isPassed = score >= ChapterTestBank.PASSING_MARKS
                        isSubmitted = true

                        // Record result and unlock class if passed!
                        studyLockManager.recordTestScore(
                            classInfo.id,
                            subjectInfo.id,
                            chapterInfo.id,
                            score
                        )

                        if (isPassed) {
                            // Update user progress metrics
                            profileManager.recordStudyProgress(1.5f, chapterPassed = true)
                        }
                    }
                ) {
                    Text("Confirm Submit", color = AnimeGlowCyan, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSubmitConfirmation = false }) {
                    Text("Cancel", color = Color.White.copy(alpha = 0.7f))
                }
            },
            containerColor = Color(0xFF1E243A)
        )
    }

    // Exit Confirmation Dialog
    if (showBackExitConfirmation) {
        AlertDialog(
            onDismissRequest = { showBackExitConfirmation = false },
            title = {
                Text("Leave Test?", color = Color.White, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    text = "Your test progress will not be submitted. Class chapters remain locked until you pass with 60+ marks.",
                    color = Color.White.copy(alpha = 0.85f)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showBackExitConfirmation = false
                        onBackClick()
                    }
                ) {
                    Text("Leave", color = Color(0xFFFF6B6B), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showBackExitConfirmation = false }) {
                    Text("Stay on Test", color = AnimeGlowCyan)
                }
            },
            containerColor = Color(0xFF1E243A)
        )
    }
}

@Composable
private fun TestHeaderBanner(
    classInfo: ClassInfo,
    subjectInfo: SubjectInfo,
    chapterInfo: ChapterInfo,
    isSubmitted: Boolean,
    onBackClick: () -> Unit
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = 10.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.12f))
                        .testTag("test_screen_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${classInfo.name} • ${subjectInfo.name}",
                        color = AnimeGlowCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Chapter ${chapterInfo.number}: ${chapterInfo.title}",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(AnimeGlowGold.copy(alpha = 0.20f))
                        .border(1.dp, AnimeGlowGold.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "80 Marks",
                        color = AnimeGlowGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Passing Threshold: 60+ Marks (75%)",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 11.5.sp
                )
                Text(
                    text = "20 Questions • 4 Marks each",
                    color = AnimeGlowCyan,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun QuestionNavigatorRow(
    totalQuestions: Int,
    currentIndex: Int,
    userAnswers: Map<Int, Int>,
    onSelectIndex: (Int) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(totalQuestions) { index ->
            val isCurrent = index == currentIndex
            val isAnswered = userAnswers.containsKey(index)

            val backgroundColor = when {
                isCurrent -> AnimeGlowGold
                isAnswered -> AnimeGlowCyan
                else -> Color.White.copy(alpha = 0.12f)
            }

            val textColor = when {
                isCurrent -> Color.Black
                isAnswered -> Color.Black
                else -> Color.White.copy(alpha = 0.85f)
            }

            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(backgroundColor)
                    .clickable { onSelectIndex(index) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${index + 1}",
                    color = textColor,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun ActiveQuestionCard(
    question: TestQuestion,
    questionIndex: Int,
    totalQuestions: Int,
    selectedOption: Int?,
    onOptionSelected: (Int) -> Unit
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = 12.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Question ${questionIndex + 1} of $totalQuestions",
                    color = AnimeGlowGold,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${question.marks} Marks",
                    color = AnimeGlowCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = question.question,
                color = Color.White,
                fontSize = 15.5.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Options List
            question.options.forEachIndexed { optionIndex, optionText ->
                val isSelected = selectedOption == optionIndex
                val optionPrefix = when (optionIndex) {
                    0 -> "A"
                    1 -> "B"
                    2 -> "C"
                    else -> "D"
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            if (isSelected) {
                                AnimeGlowCyan.copy(alpha = 0.22f)
                            } else {
                                Color.White.copy(alpha = 0.08f)
                            }
                        )
                        .border(
                            1.dp,
                            if (isSelected) AnimeGlowCyan else Color.White.copy(alpha = 0.16f),
                            RoundedCornerShape(14.dp)
                        )
                        .clickable { onOptionSelected(optionIndex) }
                        .padding(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) AnimeGlowCyan else Color.White.copy(alpha = 0.15f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = optionPrefix,
                                color = if (isSelected) Color.Black else Color.White,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = optionText,
                            color = Color.White,
                            fontSize = 13.5.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TestResultBanner(
    score: Int,
    isPassed: Boolean,
    totalQuestions: Int,
    answeredCount: Int,
    classInfo: ClassInfo,
    onBackToChapter: () -> Unit,
    onUnlockedConfirmed: () -> Unit,
    onRetake: () -> Unit
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = 14.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(CircleShape)
                    .background(
                        if (isPassed) {
                            Brush.linearGradient(listOf(AnimeGlowCyan, Color(0xFF06D6A0)))
                        } else {
                            Brush.linearGradient(listOf(Color(0xFFFF5964), AnimeGlowPink))
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isPassed) Icons.Rounded.EmojiEvents else Icons.Rounded.Cancel,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(38.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = if (isPassed) "CONGRATULATIONS!" else "NEEDS IMPROVEMENT",
                color = if (isPassed) AnimeGlowGold else Color(0xFFFF6B6B),
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "$score / ${ChapterTestBank.TOTAL_MARKS} Marks",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black
            )

            Text(
                text = "Passing Threshold: 60+ Marks (${(score * 100) / ChapterTestBank.TOTAL_MARKS}%)",
                color = Color.White.copy(alpha = 0.75f),
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (isPassed) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x3306D6A0))
                        .border(1.dp, Color(0x7706D6A0), RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.LockOpen,
                        contentDescription = null,
                        tint = Color(0xFF06D6A0),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "All chapters across ${classInfo.name} are now UNLOCKED! Completion recorded in your academic profile.",
                        color = Color.White,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x33E63946))
                        .border(1.dp, Color(0x77E63946), RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Cancel,
                        contentDescription = null,
                        tint = Color(0xFFFF6B6B),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Score below 60 marks. Other chapters for ${classInfo.name} remain LOCKED until you pass this chapter's test.",
                        color = Color.White,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Action Buttons
            if (isPassed) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Brush.horizontalGradient(listOf(AnimeGlowCyan, Color(0xFF06D6A0))))
                        .clickable(onClick = onUnlockedConfirmed)
                        .padding(vertical = 14.dp)
                        .testTag("view_unlocked_chapters_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "VIEW UNLOCKED CHAPTERS",
                        color = Color.Black,
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Brush.horizontalGradient(listOf(AnimeGlowPink, AnimeGlowCyan)))
                        .clickable(onClick = onRetake)
                        .padding(vertical = 14.dp)
                        .testTag("retake_test_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.Refresh, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "RE-ATTEMPT TEST (60+ TO UNLOCK)",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White.copy(alpha = 0.10f))
                    .clickable(onClick = onBackToChapter)
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Back to Chapter Study",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun QuestionReviewCard(
    index: Int,
    question: TestQuestion,
    userAnswer: Int?
) {
    val isCorrect = userAnswer == question.correctIndex
    val isAnswered = userAnswer != null

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = 6.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Q$index",
                    color = AnimeGlowGold,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (isCorrect) Color(0x3306D6A0) else Color(0x33E63946)
                        )
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (isCorrect) "+4 Marks (Correct)" else if (isAnswered) "0 Marks (Incorrect)" else "0 Marks (Skipped)",
                        color = if (isCorrect) Color(0xFF06D6A0) else Color(0xFFFF6B6B),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = question.question,
                color = Color.White,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Correct Answer: ${question.options[question.correctIndex]}",
                color = AnimeGlowCyan,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )

            if (!isCorrect && isAnswered) {
                Text(
                    text = "Your Answer: ${question.options[userAnswer]}",
                    color = Color(0xFFFF8A8A),
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Explanation: ${question.explanation}",
                color = Color.White.copy(alpha = 0.70f),
                fontSize = 11.5.sp,
                lineHeight = 15.sp
            )
        }
    }
}
