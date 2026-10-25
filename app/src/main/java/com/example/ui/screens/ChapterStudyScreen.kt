package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.VolumeOff
import androidx.compose.material.icons.rounded.AppBlocking
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.LockOpen
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Quiz
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.StudyLockManager
import com.example.model.ChapterHighlight
import com.example.model.ChapterInfo
import com.example.model.ClassInfo
import com.example.model.SubjectInfo
import com.example.service.StudyBlockerService
import com.example.ui.components.AnimeGlowCyan
import com.example.ui.components.AnimeGlowGold
import com.example.ui.components.AnimeGlowPink
import com.example.ui.components.AppBlockerSelectionDialog
import com.example.ui.components.GlassCard
import java.util.Locale

@Composable
fun ChapterStudyScreen(
    classInfo: ClassInfo,
    subjectInfo: SubjectInfo,
    chapterInfo: ChapterInfo,
    onBackClick: () -> Unit,
    onStartTestClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val context = LocalContext.current
    val studyLockManager = remember { StudyLockManager.getInstance(context) }

    val activeSession by studyLockManager.activeSession.collectAsStateWithLifecycle()
    val isAlarmRinging by studyLockManager.isAlarmRinging.collectAsStateWithLifecycle()
    val completedChapters by studyLockManager.completedChapters.collectAsStateWithLifecycle()

    val isCurrentChapterSession = activeSession?.chapterId == chapterInfo.id
    val isChapterCompleted = completedChapters[chapterInfo.id]?.isPassed == true
    val testResult = completedChapters[chapterInfo.id]

    // Editable study duration state (minimum 25 min)
    var customDurationText by remember { mutableStateOf("25") }
    var selectedDurationMinutes by remember { mutableIntStateOf(25) }
    var durationError by remember { mutableStateOf<String?>(null) }
    var showAppBlockerDialog by remember { mutableStateOf(false) }

    // Track if user clicked Chapter Complete to reveal inline Revision & Test options
    var isInlineActionsRevealed by remember { mutableStateOf(isChapterCompleted) }

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
            // Header: Back navigation, Subject & Class breadcrumbs, Large Chapter Title
            item {
                ChapterHeroBanner(
                    classInfo = classInfo,
                    subjectInfo = subjectInfo,
                    chapterInfo = chapterInfo,
                    isCompleted = isChapterCompleted,
                    testScore = testResult?.score,
                    onBackClick = onBackClick
                )
            }

            // Highlights Section Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.AutoAwesome,
                            contentDescription = null,
                            tint = AnimeGlowGold,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Chapter Highlights & Key Concepts",
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "${chapterInfo.highlights.size} Core Concepts",
                        color = Color.White.copy(alpha = 0.65f),
                        fontSize = 11.sp
                    )
                }
            }

            // Highlights Cards
            items(chapterInfo.highlights) { highlight ->
                HighlightCard(highlight = highlight)
            }

            // Study Timer Section with App Blocker, Alarm & Completion Flow
            item {
                StudyTimerControlSection(
                    chapterInfo = chapterInfo,
                    isCurrentSessionActive = isCurrentChapterSession,
                    activeSession = activeSession,
                    isAlarmRinging = isAlarmRinging,
                    isChapterCompleted = isChapterCompleted,
                    selectedDurationMinutes = selectedDurationMinutes,
                    customDurationText = customDurationText,
                    durationError = durationError,
                    onDurationTextChange = { text ->
                        customDurationText = text
                        val parsed = text.toIntOrNull()
                        if (parsed == null) {
                            durationError = "Please enter valid minutes"
                        } else if (parsed < StudyLockManager.MINIMUM_STUDY_DURATION_MINUTES) {
                            durationError = "Minimum study duration is 25 minutes"
                            selectedDurationMinutes = parsed
                        } else {
                            durationError = null
                            selectedDurationMinutes = parsed
                        }
                    },
                    onSelectPresetMinutes = { mins ->
                        selectedDurationMinutes = mins
                        customDurationText = mins.toString()
                        durationError = null
                    },
                    onOpenAppBlocker = {
                        val parsed = customDurationText.toIntOrNull() ?: 25
                        if (parsed < StudyLockManager.MINIMUM_STUDY_DURATION_MINUTES) {
                            durationError = "Minimum study duration is 25 minutes"
                        } else {
                            durationError = null
                            selectedDurationMinutes = parsed
                            showAppBlockerDialog = true
                        }
                    },
                    onSilenceAlarm = {
                        StudyBlockerService.silenceAlarm(context)
                    },
                    isInlineActionsRevealed = isInlineActionsRevealed,
                    onChapterCompleteClicked = {
                        isInlineActionsRevealed = true
                    },
                    onRevisionClicked = {
                        val parsed = customDurationText.toIntOrNull() ?: 25
                        selectedDurationMinutes = parsed.coerceAtLeast(25)
                        showAppBlockerDialog = true
                    },
                    onStartTestClicked = onStartTestClick,
                    previousTestResult = testResult
                )
            }
        }
    }

    // App Blocker Selection Modal
    if (showAppBlockerDialog) {
        AppBlockerSelectionDialog(
            studyDurationMinutes = selectedDurationMinutes,
            chapterTitle = chapterInfo.title,
            onDismiss = { showAppBlockerDialog = false },
            onStartConfirmed = { blockedPackages ->
                showAppBlockerDialog = false
                // Start session in StudyLockManager (enforces class-wide lock if incomplete)
                studyLockManager.startStudySession(
                    classId = classInfo.id,
                    subjectId = subjectInfo.id,
                    chapterId = chapterInfo.id,
                    chapterTitle = chapterInfo.title,
                    durationMinutes = selectedDurationMinutes,
                    blockedPackages = blockedPackages
                )
                // Start Foreground Service for persistent countdown, notification and app blocking
                StudyBlockerService.startService(context)
            }
        )
    }
}

@Composable
private fun ChapterHeroBanner(
    classInfo: ClassInfo,
    subjectInfo: SubjectInfo,
    chapterInfo: ChapterInfo,
    isCompleted: Boolean,
    testScore: Int?,
    onBackClick: () -> Unit
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
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.12f))
                        .border(1.dp, Color.White.copy(alpha = 0.25f), CircleShape)
                        .testTag("study_screen_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back to Chapter List",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${classInfo.name} • ${subjectInfo.name}",
                        color = AnimeGlowCyan,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Chapter ${chapterInfo.number}",
                        color = AnimeGlowGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                if (isCompleted) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x3306D6A0))
                            .border(1.dp, Color(0xFF06D6A0), RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF06D6A0),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "PASSED ${testScore ?: 60}/80",
                                color = Color(0xFF06D6A0),
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Prominent Chapter Title
            Text(
                text = chapterInfo.title,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.4.sp,
                lineHeight = 30.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = chapterInfo.summary,
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun HighlightCard(highlight: ChapterHighlight) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = 6.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Brush.linearGradient(listOf(AnimeGlowPink, AnimeGlowCyan))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.BookmarkBorder,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = highlight.title,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Category pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White.copy(alpha = 0.12f))
                        .border(1.dp, Color.White.copy(alpha = 0.20f), RoundedCornerShape(10.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = highlight.category,
                        color = AnimeGlowGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = highlight.content,
                color = Color.White.copy(alpha = 0.88f),
                fontSize = 13.5.sp,
                lineHeight = 19.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Composable
private fun StudyTimerControlSection(
    chapterInfo: ChapterInfo,
    isCurrentSessionActive: Boolean,
    activeSession: com.example.data.ActiveStudySession?,
    isAlarmRinging: Boolean,
    isChapterCompleted: Boolean,
    selectedDurationMinutes: Int,
    customDurationText: String,
    durationError: String?,
    onDurationTextChange: (String) -> Unit,
    onSelectPresetMinutes: (Int) -> Unit,
    onOpenAppBlocker: () -> Unit,
    onSilenceAlarm: () -> Unit,
    isInlineActionsRevealed: Boolean,
    onChapterCompleteClicked: () -> Unit,
    onRevisionClicked: () -> Unit,
    onStartTestClicked: () -> Unit,
    previousTestResult: com.example.data.ChapterTestResult?
) {
    val isTimerRunning = isCurrentSessionActive && activeSession?.isRunning == true
    val isTimerFinished = isCurrentSessionActive && (activeSession?.isTimerCompleted == true || (activeSession?.remainingSeconds ?: 1) <= 0)
    val remainingSec = if (isCurrentSessionActive && activeSession != null) activeSession.remainingSeconds else selectedDurationMinutes * 60

    val formattedTime = remember(remainingSec) {
        val m = remainingSec / 60
        val s = remainingSec % 60
        String.format(Locale.US, "%02d:%02d", m, s)
    }

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = 14.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Timer,
                    contentDescription = null,
                    tint = AnimeGlowCyan,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "STUDY TIMER & APP BLOCKER",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Non-cancellable badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0x33FFB703))
                    .border(1.dp, Color(0x77FFB703), RoundedCornerShape(10.dp))
                    .padding(horizontal = 10.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "Strict Non-Cancellable Focus (Min. 25 Mins)",
                    color = AnimeGlowGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Circular Countdown Display
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(
                                if (isAlarmRinging) Color(0x66FF5964) else Color.White.copy(alpha = 0.12f),
                                Color(0x30101833),
                                Color.Transparent
                            )
                        )
                    )
                    .border(
                        3.dp,
                        if (isAlarmRinging) {
                            Brush.sweepGradient(listOf(Color(0xFFFF5964), AnimeGlowGold, Color(0xFFFF5964)))
                        } else if (isTimerRunning) {
                            Brush.sweepGradient(listOf(AnimeGlowPink, AnimeGlowCyan, AnimeGlowGold, AnimeGlowPink))
                        } else {
                            Brush.sweepGradient(listOf(Color.White.copy(alpha = 0.3f), Color.White.copy(alpha = 0.5f)))
                        },
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = formattedTime,
                        color = Color.White,
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 2.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = when {
                            isAlarmRinging -> "ALARM RINGING!"
                            isTimerFinished -> "COMPLETED"
                            isTimerRunning -> "ACTIVE FOCUS"
                            else -> "TARGET DURATION"
                        },
                        color = if (isAlarmRinging) Color(0xFFFF6B6B) else AnimeGlowGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Loud Alarm banner when ringing
            if (isAlarmRinging) {
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x44FF5964))
                        .border(1.dp, Color(0xFFFF5964), RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.NotificationsActive,
                            contentDescription = null,
                            tint = Color(0xFFFF6B6B),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Timer Reached 00:00! Alarm ringing.",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White.copy(alpha = 0.25f))
                            .clickable(onClick = onSilenceAlarm)
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.AutoMirrored.Rounded.VolumeOff, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Silence", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Duration Setup (Editable when not actively running)
            if (!isTimerRunning) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Set Study Duration (Minutes):",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = customDurationText,
                            onValueChange = onDurationTextChange,
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AnimeGlowCyan,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.25f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                cursorColor = AnimeGlowCyan
                            ),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            trailingIcon = {
                                Text("min", color = AnimeGlowCyan, fontSize = 12.sp, modifier = Modifier.padding(end = 12.dp))
                            }
                        )

                        // Quick presets chips row
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.weight(1.8f)
                        ) {
                            val presets = listOf(25, 30, 45, 60, 90)
                            items(presets) { mins ->
                                val isSelected = selectedDurationMinutes == mins
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { onSelectPresetMinutes(mins) },
                                    label = { Text("${mins}m", fontSize = 12.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        containerColor = Color.White.copy(alpha = 0.08f),
                                        selectedContainerColor = AnimeGlowCyan.copy(alpha = 0.35f),
                                        labelColor = Color.White,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }

                    if (durationError != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = durationError,
                            color = Color(0xFFFF6B6B),
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // START STUDY BUTTON
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Brush.horizontalGradient(listOf(AnimeGlowPink, AnimeGlowCyan)))
                            .clickable(onClick = onOpenAppBlocker)
                            .padding(vertical = 14.dp)
                            .testTag("start_study_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.PlayArrow,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "START STUDY • SELECT BLOCKED APPS",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
            } else {
                // When timer is running: Display lock warning and blocked app counter
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val blockedCount = activeSession?.blockedPackages?.size ?: 0
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.08f))
                            .border(1.dp, Color.White.copy(alpha = 0.16f), RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.AppBlocking,
                            contentDescription = null,
                            tint = AnimeGlowCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Persistent Foreground Service Running",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "$blockedCount apps blocked. Session will continue if you leave or close the app.",
                                color = Color.White.copy(alpha = 0.75f),
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "🔒 Non-cancellable: Cannot be stopped or paused until 00:00 is reached.",
                        color = AnimeGlowGold,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // CHAPTER COMPLETE BUTTON
            // Strictly disabled until the minimum timer finishes!
            val canComplete = isTimerFinished || isChapterCompleted
            val completeButtonBackground = if (canComplete) {
                Brush.horizontalGradient(listOf(Color(0xFF06D6A0), AnimeGlowCyan))
            } else {
                Brush.horizontalGradient(listOf(Color.White.copy(alpha = 0.08f), Color.White.copy(alpha = 0.08f)))
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(completeButtonBackground)
                    .border(
                        1.dp,
                        if (canComplete) Color(0xFF06D6A0) else Color.White.copy(alpha = 0.15f),
                        RoundedCornerShape(16.dp)
                    )
                    .clickable(enabled = canComplete) {
                        onChapterCompleteClicked()
                    }
                    .padding(vertical = 14.dp)
                    .testTag("chapter_complete_btn"),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.CheckCircle,
                        contentDescription = null,
                        tint = if (canComplete) Color.Black else Color.White.copy(alpha = 0.4f),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isChapterCompleted) "Chapter Completed (Passed)" else "Chapter Complete",
                        color = if (canComplete) Color.Black else Color.White.copy(alpha = 0.45f),
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (!canComplete) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Requires minimum 25-minute study timer completion to activate.",
                    color = Color.White.copy(alpha = 0.55f),
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center
                )
            }

            // INLINE ACTIONS: Once completed, reveal "Revision" and "Test" options inline on the same screen!
            if (isInlineActionsRevealed || canComplete) {
                Spacer(modifier = Modifier.height(18.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.06f))
                        .border(1.dp, AnimeGlowCyan.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Next Actions Available",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Choose Revision to start a new timed focus block, or take the 80-mark chapter test to unlock the class level.",
                        color = Color.White.copy(alpha = 0.75f),
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // REVISION BUTTON: Opens timer and app selection for a new >= 25 min session
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color.White.copy(alpha = 0.12f))
                                .border(1.dp, AnimeGlowGold.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                                .clickable(onClick = onRevisionClicked)
                                .padding(vertical = 12.dp)
                                .testTag("revision_btn"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Rounded.Refresh,
                                    contentDescription = null,
                                    tint = AnimeGlowGold,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Revision",
                                    color = AnimeGlowGold,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // TEST BUTTON: Launches 80-mark chapter test
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Brush.horizontalGradient(listOf(AnimeGlowPink, AnimeGlowCyan)))
                                .clickable(onClick = onStartTestClicked)
                                .padding(vertical = 12.dp)
                                .testTag("chapter_test_btn"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Rounded.Quiz,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Test (80 M)",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    if (previousTestResult != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Latest Test Score: ${previousTestResult.score}/80 • ${if (previousTestResult.isPassed) "Passed (Class Unlocked)" else "Needs 60+ to Unlock"}",
                                color = if (previousTestResult.isPassed) Color(0xFF06D6A0) else Color(0xFFFF6B6B),
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}
