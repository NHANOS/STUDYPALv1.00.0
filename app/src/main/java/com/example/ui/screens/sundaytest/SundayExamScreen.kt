package com.example.ui.screens.sundaytest

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.NavigateBefore
import androidx.compose.material.icons.automirrored.rounded.NavigateNext
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.SundayQuestionType
import com.example.model.SundayTestPaper
import com.example.model.SundayTestQuestion
import com.example.model.SundayUserResponse
import com.example.ui.components.AnimeGlowCyan
import com.example.ui.components.AnimeGlowGold
import com.example.ui.components.AnimeGlowPink
import com.example.ui.components.GlassCard
import kotlinx.coroutines.delay

@Composable
fun SundayExamScreen(
    paper: SundayTestPaper,
    onBackExit: () -> Unit,
    onSubmitExam: (responses: Map<String, SundayUserResponse>, timeTakenSeconds: Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    val totalQuestions = paper.questions.size

    // User responses storage: questionId -> SundayUserResponse
    val userResponses = remember { mutableStateMapOf<String, SundayUserResponse>() }

    // Strict 3-Hour Countdown Timer (10,800 seconds)
    var remainingSeconds by remember { mutableIntStateOf(paper.durationSeconds) }
    var timeTakenSeconds by remember { mutableIntStateOf(0) }

    // Dialog flags
    var showSubmitDialog by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }
    var showQuestionPalette by remember { mutableStateOf(false) }

    // Auto-countdown ticker
    LaunchedEffect(Unit) {
        while (remainingSeconds > 0) {
            delay(1000L)
            remainingSeconds--
            timeTakenSeconds++
        }
        // Auto-submit immediately upon timer expiration!
        onSubmitExam(userResponses.toMap(), timeTakenSeconds)
    }

    // Intercept hardware / gesture back button
    BackHandler {
        showExitDialog = true
    }

    val currentQuestion = paper.questions[currentQuestionIndex]
    val currentResponse = userResponses[currentQuestion.id]

    // Local transient states for the active question on screen
    var selectedOptionIndex by remember(currentQuestion.id) {
        mutableStateOf(currentResponse?.selectedOptionIndex)
    }
    var subjectiveText by remember(currentQuestion.id) {
        mutableStateOf(currentResponse?.textAnswer ?: "")
    }

    // Helper to save current response before moving
    fun persistActiveResponse(isSkipping: Boolean = false) {
        if (isSkipping) {
            userResponses[currentQuestion.id] = SundayUserResponse(
                questionId = currentQuestion.id,
                selectedOptionIndex = null,
                textAnswer = "",
                isSkipped = true
            )
        } else {
            val hasAnswer = (selectedOptionIndex != null) || subjectiveText.trim().isNotEmpty()
            userResponses[currentQuestion.id] = SundayUserResponse(
                questionId = currentQuestion.id,
                selectedOptionIndex = selectedOptionIndex,
                textAnswer = subjectiveText.trim(),
                isSkipped = !hasAnswer
            )
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 680.dp)
                .padding(
                    top = contentPadding.calculateTopPadding() + 8.dp,
                    bottom = contentPadding.calculateBottomPadding() + 16.dp,
                    start = 16.dp,
                    end = 16.dp
                )
        ) {
            // Top Bar: Timer, Subject, Section, Palette Button
            ExamTopBar(
                paper = paper,
                currentQuestion = currentQuestion,
                currentQuestionIndex = currentQuestionIndex,
                totalQuestions = totalQuestions,
                remainingSeconds = remainingSeconds,
                onOpenPalette = { showQuestionPalette = true },
                onExitClick = { showExitDialog = true }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Main Single-Question Container (Strictly one question per screen)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                SingleQuestionView(
                    question = currentQuestion,
                    questionIndex = currentQuestionIndex,
                    totalQuestions = totalQuestions,
                    selectedOptionIndex = selectedOptionIndex,
                    subjectiveText = subjectiveText,
                    onOptionSelected = { selectedOptionIndex = it },
                    onSubjectiveTextChanged = { subjectiveText = it }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom Navigation Controls: Previous, Skip, Next, Submit
            ExamBottomNavigation(
                currentIndex = currentQuestionIndex,
                totalQuestions = totalQuestions,
                onPrevious = {
                    persistActiveResponse(isSkipping = false)
                    if (currentQuestionIndex > 0) {
                        currentQuestionIndex--
                    }
                },
                onSkip = {
                    persistActiveResponse(isSkipping = true)
                    if (currentQuestionIndex < totalQuestions - 1) {
                        currentQuestionIndex++
                    } else {
                        showSubmitDialog = true
                    }
                },
                onNext = {
                    persistActiveResponse(isSkipping = false)
                    if (currentQuestionIndex < totalQuestions - 1) {
                        currentQuestionIndex++
                    } else {
                        showSubmitDialog = true
                    }
                },
                onSubmit = {
                    persistActiveResponse(isSkipping = false)
                    showSubmitDialog = true
                }
            )
        }

        // Question Palette Modal Sheet
        if (showQuestionPalette) {
            QuestionPaletteDialog(
                questions = paper.questions,
                currentIndex = currentQuestionIndex,
                userResponses = userResponses,
                onDismiss = { showQuestionPalette = false },
                onSelectQuestion = { targetIndex ->
                    persistActiveResponse(isSkipping = false)
                    currentQuestionIndex = targetIndex
                    showQuestionPalette = false
                }
            )
        }

        // Submit Confirmation Modal
        if (showSubmitDialog) {
            val answeredCount = userResponses.values.count { !it.isSkipped && (it.selectedOptionIndex != null || it.textAnswer.isNotEmpty()) }
            val skippedCount = totalQuestions - answeredCount

            AlertDialog(
                onDismissRequest = { showSubmitDialog = false },
                title = {
                    Text(
                        text = "Submit CBSE Sunday Exam?",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                text = {
                    Column {
                        Text(
                            text = "You have answered $answeredCount of $totalQuestions questions.",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 13.5.sp
                        )
                        if (skippedCount > 0) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Notice: $skippedCount question(s) are skipped or blank.",
                                color = AnimeGlowGold,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Upon submission, your answers will undergo the Two-Layer Evaluation Flow (Layer 1 Internal AI + Layer 2 Web-Grounded Verification).",
                            color = AnimeGlowCyan,
                            fontSize = 12.sp
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showSubmitDialog = false
                            onSubmitExam(userResponses.toMap(), timeTakenSeconds)
                        },
                        modifier = Modifier.testTag("confirm_submit_exam_btn")
                    ) {
                        Text("Confirm Submit", color = AnimeGlowCyan, fontWeight = FontWeight.ExtraBold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showSubmitDialog = false }) {
                        Text("Review Answers", color = Color.White.copy(alpha = 0.7f))
                    }
                },
                containerColor = Color(0xFF161B2E)
            )
        }

        // Exit / Abandon Confirmation
        if (showExitDialog) {
            AlertDialog(
                onDismissRequest = { showExitDialog = false },
                title = {
                    Text("Quit Examination?", color = Color.White, fontWeight = FontWeight.Bold)
                },
                text = {
                    Text(
                        text = "If you leave now, this attempt will count as one of your 3 Sunday attempts and the 2-hour rest cooldown will begin.",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 13.sp
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showExitDialog = false
                            onBackExit()
                        }
                    ) {
                        Text("Exit Test", color = Color(0xFFFF6B6B), fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showExitDialog = false }) {
                        Text("Stay on Exam", color = AnimeGlowCyan)
                    }
                },
                containerColor = Color(0xFF161B2E)
            )
        }
    }
}

@Composable
private fun ExamTopBar(
    paper: SundayTestPaper,
    currentQuestion: SundayTestQuestion,
    currentQuestionIndex: Int,
    totalQuestions: Int,
    remainingSeconds: Int,
    onOpenPalette: () -> Unit,
    onExitClick: () -> Unit
) {
    val hours = remainingSeconds / 3600
    val minutes = (remainingSeconds % 3600) / 60
    val seconds = remainingSeconds % 60
    val formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds)
    val isLowTime = remainingSeconds < 15 * 60 // under 15 minutes

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${paper.className} • ${paper.subjectName}",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${currentQuestion.section.title}: ${currentQuestion.section.description}",
                    color = AnimeGlowCyan,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Timer Chip
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isLowTime) Color(0x44FF3B30) else Color(0x33000000))
                        .border(
                            1.dp,
                            if (isLowTime) Color(0xFFFF3B30) else AnimeGlowGold.copy(alpha = 0.5f),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.Timer,
                            contentDescription = "Time Remaining",
                            tint = if (isLowTime) Color(0xFFFF6B6B) else AnimeGlowGold,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = formattedTime,
                            color = if (isLowTime) Color(0xFFFF6B6B) else Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.testTag("exam_timer_display")
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Question Palette Grid Icon
                IconButton(
                    onClick = onOpenPalette,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.12f))
                        .testTag("open_question_palette_btn")
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Apps,
                        contentDescription = "View Question Palette",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                // Exit
                IconButton(
                    onClick = onExitClick,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.12f))
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Exit Exam",
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

/**
 * Single question view rendered one by one.
 */
@Composable
private fun SingleQuestionView(
    question: SundayTestQuestion,
    questionIndex: Int,
    totalQuestions: Int,
    selectedOptionIndex: Int?,
    subjectiveText: String,
    onOptionSelected: (Int) -> Unit,
    onSubjectiveTextChanged: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    GlassCard(
        modifier = Modifier.fillMaxSize(),
        elevation = 12.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(18.dp)
        ) {
            // Top Indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(AnimeGlowGold.copy(alpha = 0.22f))
                        .border(1.dp, AnimeGlowGold.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 9.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "Question ${questionIndex + 1} of $totalQuestions",
                        color = AnimeGlowGold,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(AnimeGlowCyan.copy(alpha = 0.22f))
                        .border(1.dp, AnimeGlowCyan.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 9.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "${question.marks} Mark${if (question.marks > 1) "s" else ""}",
                        color = AnimeGlowCyan,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Case Study Context Box (if applicable)
            if (question.caseStudyContext != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.08f))
                        .border(1.dp, AnimeGlowPink.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Text(
                            text = "CASE STUDY DATA / SOURCE",
                            color = AnimeGlowPink,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = question.caseStudyContext,
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 12.5.sp,
                            lineHeight = 17.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // Question Prompt Text
            Text(
                text = question.question,
                color = Color.White,
                fontSize = 16.sp,
                lineHeight = 23.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.testTag("question_prompt_text")
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Response Input Handling
            if (question.type == SundayQuestionType.MCQ) {
                // RENDER SELECTABLE RADIO OPTIONS
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    question.options.forEachIndexed { optIdx, optText ->
                        val isSelected = selectedOptionIndex == optIdx
                        val optionPrefix = when (optIdx) {
                            0 -> "A"
                            1 -> "B"
                            2 -> "C"
                            else -> "D"
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    if (isSelected) AnimeGlowCyan.copy(alpha = 0.22f)
                                    else Color.White.copy(alpha = 0.07f)
                                )
                                .border(
                                    1.dp,
                                    if (isSelected) AnimeGlowCyan else Color.White.copy(alpha = 0.18f),
                                    RoundedCornerShape(14.dp)
                                )
                                .clickable { onOptionSelected(optIdx) }
                                .padding(12.dp)
                                .testTag("option_item_$optIdx")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { onOptionSelected(optIdx) },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = AnimeGlowCyan,
                                        unselectedColor = Color.White.copy(alpha = 0.45f)
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "$optionPrefix. $optText",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    lineHeight = 19.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            } else {
                // DEDICATED TEXT/WRITING INPUT AREA FOR DESCRIPTIVE/SUBJECTIVE
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Write your descriptive answer below:",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        val wordCount = if (subjectiveText.trim().isEmpty()) 0 else subjectiveText.trim().split("\\s+".toRegex()).size
                        Text(
                            text = "$wordCount words",
                            color = AnimeGlowCyan,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = subjectiveText,
                        onValueChange = onSubjectiveTextChanged,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .testTag("subjective_answer_input"),
                        placeholder = {
                            Text(
                                text = "Type your comprehensive answer, derivations, key steps, or explanations here...",
                                color = Color.White.copy(alpha = 0.40f),
                                fontSize = 13.5.sp
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = AnimeGlowCyan,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.25f),
                            cursorColor = AnimeGlowCyan
                        ),
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "CBSE Rubric Tip: Mention specific laws, formulas, and definitions for full credit.",
                        color = AnimeGlowGold,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

/**
 * Bottom navigation controls: Previous, Skip, Next, Submit.
 */
@Composable
private fun ExamBottomNavigation(
    currentIndex: Int,
    totalQuestions: Int,
    onPrevious: () -> Unit,
    onSkip: () -> Unit,
    onNext: () -> Unit,
    onSubmit: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Previous Button
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White.copy(alpha = if (currentIndex > 0) 0.14f else 0.05f))
                .border(1.dp, Color.White.copy(alpha = if (currentIndex > 0) 0.25f else 0.08f), RoundedCornerShape(12.dp))
                .clickable(enabled = currentIndex > 0, onClick = onPrevious)
                .padding(vertical = 12.dp)
                .testTag("exam_previous_btn"),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.NavigateBefore,
                    contentDescription = null,
                    tint = if (currentIndex > 0) Color.White else Color.White.copy(alpha = 0.3f),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Previous",
                    color = if (currentIndex > 0) Color.White else Color.White.copy(alpha = 0.3f),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Skip Button
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0x33FF9F1C))
                .border(1.dp, Color(0x77FF9F1C), RoundedCornerShape(12.dp))
                .clickable(onClick = onSkip)
                .padding(vertical = 12.dp)
                .testTag("exam_skip_btn"),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Rounded.SkipNext,
                    contentDescription = null,
                    tint = AnimeGlowGold,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Skip",
                    color = AnimeGlowGold,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Next Button or Submit Button
        if (currentIndex < totalQuestions - 1) {
            Box(
                modifier = Modifier
                    .weight(1.2f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Brush.horizontalGradient(listOf(AnimeGlowPink, AnimeGlowCyan)))
                    .clickable(onClick = onNext)
                    .padding(vertical = 12.dp)
                    .testTag("exam_next_btn"),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Next",
                        color = Color.White,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.NavigateNext,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .weight(1.3f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Brush.horizontalGradient(listOf(AnimeGlowGold, AnimeGlowPink)))
                    .clickable(onClick = onSubmit)
                    .padding(vertical = 12.dp)
                    .testTag("exam_submit_btn"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "SUBMIT EXAM",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

/**
 * Question Palette Dialog modal allowing overview of all questions.
 */
@Composable
private fun QuestionPaletteDialog(
    questions: List<SundayTestQuestion>,
    currentIndex: Int,
    userResponses: Map<String, SundayUserResponse>,
    onDismiss: () -> Unit,
    onSelectQuestion: (Int) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = Color(0xF2101426),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.35f)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
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
                        text = "Question Palette",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Rounded.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Legend
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    LegendItem(color = Color(0xFF06D6A0), label = "Answered")
                    LegendItem(color = AnimeGlowGold, label = "Skipped")
                    LegendItem(color = Color.White.copy(alpha = 0.2f), label = "Not Visited")
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Grid of 38 questions
                LazyVerticalGrid(
                    columns = GridCells.Fixed(6),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                ) {
                    items(questions.size) { idx ->
                        val q = questions[idx]
                        val resp = userResponses[q.id]
                        val isCurrent = idx == currentIndex
                        val isAnswered = resp != null && !resp.isSkipped && (resp.selectedOptionIndex != null || resp.textAnswer.isNotEmpty())
                        val isSkipped = resp != null && resp.isSkipped

                        val bg = when {
                            isCurrent -> AnimeGlowCyan
                            isAnswered -> Color(0xFF06D6A0)
                            isSkipped -> AnimeGlowGold
                            else -> Color.White.copy(alpha = 0.12f)
                        }

                        val textColor = if (isCurrent || isAnswered || isSkipped) Color.Black else Color.White

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(bg)
                                .clickable { onSelectQuestion(idx) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${idx + 1}",
                                color = textColor,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.75f),
            fontSize = 11.sp
        )
    }
}
