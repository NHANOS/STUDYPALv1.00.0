package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.Assessment
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.CheckCircleOutline
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.HourglassEmpty
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Quiz
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.ChapterTestResult
import com.example.data.StudyLockManager
import com.example.data.SundayTestManager
import com.example.model.ChapterInfo
import com.example.model.ClassInfo
import com.example.model.StudyRepository
import com.example.model.SubjectInfo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

// STUDYPAL Dedicated Theme Colors
val ThemeBackgroundDark = Color(0xFF0A0909)
val ThemeCardDark = Color(0xFF141419)
val ThemeSilverBorderTop = Color(0xFFE2E2E8)
val ThemeSilverBorderBottom = Color(0xFF4A4A54)
val AccentPassedGreen = Color(0xFF06D6A0)
val AccentRemainingSilver = Color(0xFF8E8E98)
val AccentCyan = Color(0xFF64D2FF)
val AccentGold = Color(0xFFFFD166)

val SilverMetallicBorder = BorderStroke(
    1.dp,
    Brush.verticalGradient(
        colors = listOf(
            ThemeSilverBorderTop.copy(alpha = 0.55f),
            ThemeSilverBorderBottom.copy(alpha = 0.25f)
        )
    )
)

data class PassedChapterRecord(
    val chapterId: String,
    val chapterNumber: Int,
    val chapterTitle: String,
    val subjectId: String,
    val subjectName: String,
    val classId: String,
    val className: String,
    val score: Int,
    val maxScore: Int,
    val timestamp: Long,
    val formattedDate: String
)

data class SubjectRemainingSummary(
    val subjectId: String,
    val subjectName: String,
    val passedCount: Int,
    val totalCount: Int,
    val pendingChapters: List<ChapterInfo>
)

@Composable
fun ProgressTrackerScreen(
    onNavigateToClass: (String) -> Unit = {},
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val context = LocalContext.current
    val studyLockManager = remember { StudyLockManager.getInstance(context) }
    val sundayTestManager = remember { SundayTestManager.getInstance(context) }

    // 100% Local Device Storage: Read strictly from local SharedPreferences
    val completedChaptersMap by studyLockManager.completedChapters.collectAsStateWithLifecycle()
    val sundayResults by sundayTestManager.savedResults.collectAsStateWithLifecycle()

    val classes = StudyRepository.classes
    var selectedClassId by remember { mutableStateOf(classes.firstOrNull()?.id ?: "class_10") }
    val selectedClass = classes.find { it.id == selectedClassId } ?: classes.first()

    // -------------------------------------------------------------
    // STRICT PASSED-DATA-ONLY FILTERING RULE:
    // Only tests where score >= 60 out of 80 (75%+) AND isPassed == true
    // Failed tests (<60) and incomplete chapters are strictly ignored!
    // -------------------------------------------------------------
    val passedResultsList = remember(completedChaptersMap, selectedClassId) {
        val list = mutableListOf<PassedChapterRecord>()
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

        for ((chapterId, result) in completedChaptersMap) {
            // Strict filtering rule:
            if (result.isPassed && result.score >= 60) {
                val loc = StudyRepository.findChapter(chapterId)
                if (loc != null) {
                    val (c, s, ch) = loc
                    if (c.id == selectedClassId) {
                        list.add(
                            PassedChapterRecord(
                                chapterId = chapterId,
                                chapterNumber = ch.number,
                                chapterTitle = ch.title,
                                subjectId = s.id,
                                subjectName = s.name,
                                classId = c.id,
                                className = c.name,
                                score = result.score,
                                maxScore = result.maxScore,
                                timestamp = result.timestamp,
                                formattedDate = dateFormat.format(Date(result.timestamp))
                            )
                        )
                    }
                }
            }
        }
        list.sortedByDescending { it.timestamp }
    }

    // Syllabus totals for the selected class
    val allClassChapters = remember(selectedClassId) {
        StudyRepository.getAllChapters(selectedClassId)
    }
    val totalSyllabusChapters = allClassChapters.size
    val passedCount = passedResultsList.size
    val remainingCount = (totalSyllabusChapters - passedCount).coerceAtLeast(0)
    val passedPercentage = if (totalSyllabusChapters > 0) {
        (passedCount.toFloat() / totalSyllabusChapters.toFloat()) * 100f
    } else 0f
    val remainingPercentage = (100f - passedPercentage).coerceAtLeast(0f)

    // Subject-wise pending syllabus summary
    val passedChapterIds = remember(passedResultsList) {
        passedResultsList.map { it.chapterId }.toSet()
    }

    val subjectSummaries = remember(selectedClass, passedChapterIds) {
        selectedClass.subjects.map { subj ->
            val totalInSubject = subj.chapters.size
            val passedInSubject = subj.chapters.count { passedChapterIds.contains(it.id) }
            val pendingList = subj.chapters.filter { !passedChapterIds.contains(it.id) }
            SubjectRemainingSummary(
                subjectId = subj.id,
                subjectName = subj.name,
                passedCount = passedInSubject,
                totalCount = totalInSubject,
                pendingChapters = pendingList
            )
        }
    }

    // Sunday mock test passed results (60+ marks)
    val passedSundayTests = remember(sundayResults, selectedClassId) {
        sundayResults.filter { it.isPassed && it.score >= 60 && it.classId == selectedClassId }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ThemeBackgroundDark),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 680.dp),
            contentPadding = PaddingValues(
                top = contentPadding.calculateTopPadding() + 10.dp,
                bottom = contentPadding.calculateBottomPadding() + 24.dp,
                start = 16.dp,
                end = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Banner
            item {
                ProgressHeaderBanner(
                    selectedClass = selectedClass,
                    classes = classes,
                    selectedClassId = selectedClassId,
                    onClassSelected = { selectedClassId = it }
                )
            }

            // Developer / Demo Test Simulation Card (allows verifying pie chart and breakdown instantly)
            item {
                DevSimulationBar(
                    onSimulatePassedTest = {
                        val firstUnpassed = allClassChapters.find { !passedChapterIds.contains(it.second.id) }
                            ?: allClassChapters.firstOrNull()
                        if (firstUnpassed != null) {
                            studyLockManager.recordTestScore(
                                classId = selectedClassId,
                                subjectId = firstUnpassed.first.id,
                                chapterId = firstUnpassed.second.id,
                                score = (68..78).random()
                            )
                        }
                    },
                    onResetProgress = {
                        studyLockManager.clearAllCompletedChapters()
                    }
                )
            }

            // Format A: Visual Donut / Pie Chart View
            item {
                PieChartViewCard(
                    passedCount = passedCount,
                    totalCount = totalSyllabusChapters,
                    remainingCount = remainingCount,
                    passedPercentage = passedPercentage,
                    remainingPercentage = remainingPercentage,
                    className = selectedClass.name
                )
            }

            // Overall Status Text Breakdown
            item {
                OverallStatusCard(
                    passedCount = passedCount,
                    remainingCount = remainingCount,
                    totalCount = totalSyllabusChapters,
                    className = selectedClass.name
                )
            }

            // Empty State Check: If no tests passed yet
            if (passedCount == 0) {
                item {
                    EmptyStateCard(
                        onStartStudying = { onNavigateToClass(selectedClassId) }
                    )
                }
            } else {
                // Format B1: Completed Chapters List
                item {
                    CompletedChaptersSection(
                        passedChapters = passedResultsList
                    )
                }
            }

            // Format B2: Remaining / Pending Syllabus Breakdown by Subject
            if (remainingCount > 0) {
                item {
                    RemainingSyllabusSection(
                        subjectSummaries = subjectSummaries,
                        onChapterClick = { onNavigateToClass(selectedClassId) }
                    )
                }
            }

            // Optional Sunday Test Passing Metrics if available
            if (passedSundayTests.isNotEmpty()) {
                item {
                    SundayPassedSection(passedSundayTests = passedSundayTests)
                }
            }
        }
    }
}

/**
 * Top Header Banner with Class selector tabs.
 */
@Composable
private fun ProgressHeaderBanner(
    selectedClass: ClassInfo,
    classes: List<ClassInfo>,
    selectedClassId: String,
    onClassSelected: (String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = ThemeCardDark,
        border = SilverMetallicBorder,
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Academic Progress Tracker",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "100% Offline Local Device Storage • Validated 60+ Marks Only",
                        color = Color.White.copy(alpha = 0.70f),
                        fontSize = 11.5.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(AccentCyan.copy(alpha = 0.35f), AccentPassedGreen.copy(alpha = 0.35f))
                            )
                        )
                        .border(1.dp, ThemeSilverBorderTop.copy(alpha = 0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Assessment,
                        contentDescription = "Progress",
                        tint = AccentPassedGreen,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Class Tabs
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(classes, key = { it.id }) { c ->
                    val isSelected = c.id == selectedClassId
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isSelected) Brush.horizontalGradient(listOf(AccentPassedGreen, AccentCyan))
                                else Brush.linearGradient(listOf(Color.White.copy(alpha = 0.08f), Color.White.copy(alpha = 0.03f)))
                            )
                            .border(
                                1.dp,
                                if (isSelected) Color.White else ThemeSilverBorderBottom.copy(alpha = 0.4f),
                                RoundedCornerShape(12.dp)
                            )
                            .clickable { onClassSelected(c.id) }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                            .testTag("progress_class_tab_${c.id}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = c.name,
                            color = if (isSelected) Color.Black else Color.White,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Black else FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

/**
 * Format A: Circular Donut / Pie Chart View.
 * Shows Completed vs Remaining slices with center statistics.
 */
@Composable
private fun PieChartViewCard(
    passedCount: Int,
    totalCount: Int,
    remainingCount: Int,
    passedPercentage: Float,
    remainingPercentage: Float,
    className: String
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("progress_pie_chart_card"),
        shape = RoundedCornerShape(24.dp),
        color = ThemeCardDark,
        border = SilverMetallicBorder,
        shadowElevation = 10.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Format A: Syllabus Completion",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$className • CBSE",
                    color = AccentGold,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Donut Pie Chart Canvas
            val animatedPassedAngle by animateFloatAsState(
                targetValue = if (totalCount > 0) (passedPercentage / 100f) * 360f else 0f,
                animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing),
                label = "DonutAnimation"
            )

            Box(
                modifier = Modifier.size(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(
                    modifier = Modifier
                        .size(190.dp)
                        .testTag("progress_pie_chart_canvas")
                ) {
                    val strokeWidth = 24.dp.toPx()
                    val diameter = size.minDimension - strokeWidth
                    val topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f)
                    val arcSize = Size(diameter, diameter)

                    // 1. Remaining Slice / Background Track (Silver Metallic Charcoal)
                    drawArc(
                        color = Color(0xFF26262E),
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidth)
                    )

                    // Remaining Slice outer silver accent
                    drawArc(
                        color = ThemeSilverBorderBottom.copy(alpha = 0.5f),
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(width = 1.dp.toPx())
                    )

                    // 2. Completed Slice (Vibrant Mint to Cyan Gradient)
                    if (animatedPassedAngle > 0f) {
                        drawArc(
                            brush = Brush.sweepGradient(
                                listOf(AccentPassedGreen, AccentCyan, AccentPassedGreen)
                            ),
                            startAngle = -90f,
                            sweepAngle = animatedPassedAngle,
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                    }
                }

                // Center Statistics: Total Passed vs Total Remaining
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 14.dp)
                ) {
                    Text(
                        text = "${passedPercentage.roundToInt()}%",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "PASSED",
                        color = AccentPassedGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$passedCount / $totalCount",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Chapters",
                        color = Color.White.copy(alpha = 0.55f),
                        fontSize = 10.5.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Chart Legends
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White.copy(alpha = 0.05f))
                    .border(1.dp, ThemeSilverBorderBottom.copy(alpha = 0.35f), RoundedCornerShape(14.dp))
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Completed Legend
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(AccentPassedGreen)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Passed (60+ Marks)",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "$passedCount Chapters (${passedPercentage.roundToInt()}%)",
                            color = AccentPassedGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Remaining Legend
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(AccentRemainingSilver)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Remaining",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "$remainingCount Chapters (${remainingPercentage.roundToInt()}%)",
                            color = AccentRemainingSilver,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

/**
 * Format B: Overall Status Banner directly below chart.
 */
@Composable
private fun OverallStatusCard(
    passedCount: Int,
    remainingCount: Int,
    totalCount: Int,
    className: String
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("progress_overall_status_card"),
        shape = RoundedCornerShape(18.dp),
        color = ThemeCardDark,
        border = SilverMetallicBorder,
        shadowElevation = 6.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.Verified,
                        contentDescription = null,
                        tint = AccentCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Format B: Written Metrics Breakdown",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "CBSE Benchmark",
                    color = AccentGold,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Mandated Overall Status text
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.08f))
                    .border(1.dp, ThemeSilverBorderTop.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                    .padding(vertical = 12.dp, horizontal = 14.dp)
            ) {
                Text(
                    text = "Passed: $passedCount Chapters | Remaining: $remainingCount Chapters",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.3.sp,
                    modifier = Modifier.testTag("progress_overall_status_text")
                )
            }
        }
    }
}

/**
 * Format B: Completed List of Chapters.
 * Subject name, chapter name, date of passing, and validated score.
 */
@Composable
private fun CompletedChaptersSection(
    passedChapters: List<PassedChapterRecord>
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Completed Chapters (${passedChapters.size})",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Filtered (60+ Marks Only)",
                color = AccentPassedGreen,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        passedChapters.forEach { item ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .testTag("passed_chapter_item_${item.chapterId}"),
                shape = RoundedCornerShape(16.dp),
                color = ThemeCardDark,
                border = SilverMetallicBorder,
                shadowElevation = 6.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(AccentPassedGreen.copy(alpha = 0.2f))
                            .border(1.dp, AccentPassedGreen.copy(alpha = 0.5f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.CheckCircle,
                            contentDescription = "Passed",
                            tint = AccentPassedGreen,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = item.subjectName,
                                color = AccentCyan,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = item.formattedDate,
                                color = Color.White.copy(alpha = 0.6f),
                                fontSize = 11.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = "Ch ${item.chapterNumber}: ${item.chapterTitle}",
                            color = Color.White,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Mandated format: e.g. "68/80 • Passed • Double-Checked"
                        Text(
                            text = "${item.score}/80 • Passed • Double-Checked",
                            color = AccentPassedGreen,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

/**
 * Format B: Remaining / Pending Summary by Subject.
 */
@Composable
private fun RemainingSyllabusSection(
    subjectSummaries: List<SubjectRemainingSummary>,
    onChapterClick: () -> Unit
) {
    var expandedSubjectId by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Remaining Syllabus by Subject",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Pending Unlocks",
                color = AccentRemainingSilver,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        subjectSummaries.forEach { summary ->
            val pendingCount = summary.pendingChapters.size
            val isExpanded = expandedSubjectId == summary.subjectId

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {
                        expandedSubjectId = if (isExpanded) null else summary.subjectId
                    }
                    .testTag("pending_subject_${summary.subjectId}"),
                shape = RoundedCornerShape(16.dp),
                color = ThemeCardDark,
                border = SilverMetallicBorder,
                shadowElevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (pendingCount == 0) AccentPassedGreen.copy(alpha = 0.2f)
                                        else Color.White.copy(alpha = 0.10f)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (pendingCount == 0) Icons.Rounded.CheckCircle else Icons.Rounded.HourglassEmpty,
                                    contentDescription = null,
                                    tint = if (pendingCount == 0) AccentPassedGreen else AccentRemainingSilver,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = summary.subjectName,
                                    color = Color.White,
                                    fontSize = 14.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = if (pendingCount == 0) "100% Completed" else "$pendingCount / ${summary.totalCount} chapters pending",
                                    color = if (pendingCount == 0) AccentPassedGreen else AccentRemainingSilver,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Icon(
                            imageVector = if (isExpanded) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                            contentDescription = "Expand",
                            tint = Color.White.copy(alpha = 0.6f)
                        )
                    }

                    // Expandable list of remaining chapter titles
                    AnimatedVisibility(
                        visible = isExpanded && pendingCount > 0,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                        ) {
                            summary.pendingChapters.forEach { ch ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.White.copy(alpha = 0.04f))
                                        .padding(horizontal = 10.dp, vertical = 7.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Ch ${ch.number}: ${ch.title}",
                                        color = Color.White.copy(alpha = 0.85f),
                                        fontSize = 12.5.sp,
                                        modifier = Modifier.weight(1f),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(Color.White.copy(alpha = 0.08f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "Needs 60+",
                                            color = AccentGold,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Sunday Mock Test Results with 60+ marks.
 */
@Composable
private fun SundayPassedSection(
    passedSundayTests: List<com.example.model.SundayTestResult>
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Passed Sunday Mock Exams (80 Marks)",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Two-Layer Verified",
                color = AccentCyan,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        passedSundayTests.forEach { r ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(16.dp),
                color = ThemeCardDark,
                border = SilverMetallicBorder,
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "${r.subjectName} Mock Exam",
                            color = Color.White,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${r.score}/80 • Passed • Two-Layer Verified",
                            color = AccentPassedGreen,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(AccentPassedGreen.copy(alpha = 0.2f))
                            .border(1.dp, AccentPassedGreen.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${r.percentage.toInt()}%",
                            color = AccentPassedGreen,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }
    }
}

/**
 * Mandated Empty State Card:
 * Displays: "No passed tests recorded yet. Complete a chapter test with 60+ marks to start your progress tracker."
 */
@Composable
private fun EmptyStateCard(
    onStartStudying: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("progress_empty_state"),
        shape = RoundedCornerShape(22.dp),
        color = ThemeCardDark,
        border = SilverMetallicBorder,
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(26.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.08f))
                    .border(1.dp, ThemeSilverBorderTop.copy(alpha = 0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.MenuBook,
                    contentDescription = null,
                    tint = AccentGold,
                    modifier = Modifier.size(34.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Exact mandated empty state message:
            Text(
                text = "No passed tests recorded yet. Complete a chapter test with 60+ marks to start your progress tracker.",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "STUDYPAL only records mastery. Incomplete sessions and scores below 60 marks are excluded.",
                color = Color.White.copy(alpha = 0.65f),
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(Brush.horizontalGradient(listOf(AccentCyan, AccentPassedGreen)))
                    .clickable(onClick = onStartStudying)
                    .padding(horizontal = 20.dp, vertical = 12.dp)
                    .testTag("progress_start_chapter_test_btn"),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.PlayArrow,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "EXPLORE CHAPTERS & TAKE TEST",
                        color = Color.Black,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.4.sp
                    )
                }
            }
        }
    }
}

/**
 * Developer and Testing Bar:
 * Allows one-tap local simulation of passing an 80-mark chapter test (e.g. 72/80)
 * and clearing records to verify both populated and empty states immediately.
 */
@Composable
private fun DevSimulationBar(
    onSimulatePassedTest: () -> Unit,
    onResetProgress: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFF111116),
        border = BorderStroke(1.dp, ThemeSilverBorderBottom.copy(alpha = 0.4f))
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
                    text = "Offline Local Storage Controls",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Pure device-stored SharedPreferences",
                    color = Color.White.copy(alpha = 0.50f),
                    fontSize = 10.sp
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(AccentPassedGreen.copy(alpha = 0.20f))
                        .border(1.dp, AccentPassedGreen.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                        .clickable(onClick = onSimulatePassedTest)
                        .padding(horizontal = 9.dp, vertical = 6.dp)
                        .testTag("simulate_passed_test_btn")
                ) {
                    Text(
                        text = "+ Sim Pass (68+)",
                        color = AccentPassedGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White.copy(alpha = 0.10f))
                        .border(1.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(8.dp))
                        .clickable(onClick = onResetProgress)
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                        .testTag("reset_progress_records_btn")
                ) {
                    Text(
                        text = "Reset",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
