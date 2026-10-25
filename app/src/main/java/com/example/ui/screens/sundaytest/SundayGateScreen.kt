package com.example.ui.screens.sundaytest

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
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Quiz
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.SundayTestManager
import com.example.model.ClassInfo
import com.example.model.StudyRepository
import com.example.model.SubjectInfo
import com.example.model.SundayTestPaper
import com.example.service.SundayTestGenerator
import com.example.ui.components.AnimeGlowCyan
import com.example.ui.components.AnimeGlowGold
import com.example.ui.components.AnimeGlowPink
import com.example.ui.components.GlassCard
import kotlinx.coroutines.delay

@Composable
fun SundayGateScreen(
    onBackClick: () -> Unit,
    onStartExam: (SundayTestPaper) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val context = LocalContext.current
    val manager = remember { SundayTestManager.getInstance(context) }
    val isSimulationMode by manager.isSimulationMode.collectAsStateWithLifecycle()
    val attempts by manager.attempts.collectAsStateWithLifecycle()
    val pastResults by manager.savedResults.collectAsStateWithLifecycle()

    // Real-time ticking timer for countdown to Sunday or cooldown
    var countdownMillis by remember { mutableLongStateOf(manager.millisUntilNextSunday()) }
    var cooldownMillis by remember { mutableLongStateOf(manager.getCooldownRemainingMillis()) }

    LaunchedEffect(Unit) {
        while (true) {
            countdownMillis = manager.millisUntilNextSunday()
            cooldownMillis = manager.getCooldownRemainingMillis()
            delay(1000L)
        }
    }

    val isSunday = manager.isTodaySunday()
    val remainingAttempts = manager.getRemainingAttempts()
    val isCooldown = manager.isCooldownActive()

    // Class and Subject Selection states
    val classes = StudyRepository.classes
    var selectedClassId by remember { mutableStateOf(classes.firstOrNull()?.id ?: "class_10") }
    val selectedClass = classes.find { it.id == selectedClassId } ?: classes.first()
    var selectedSubjectId by remember { mutableStateOf(selectedClass.subjects.firstOrNull()?.id ?: "") }

    LaunchedEffect(selectedClassId) {
        if (selectedClass.subjects.none { it.id == selectedSubjectId }) {
            selectedSubjectId = selectedClass.subjects.firstOrNull()?.id ?: ""
        }
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
                bottom = contentPadding.calculateBottomPadding() + 24.dp,
                start = 16.dp,
                end = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item {
                SundayHeaderBanner(onBackClick = onBackClick)
            }

            // Developer / Tester Simulation Mode Toggle Card
            item {
                SimulationToggleCard(
                    isSimulationMode = isSimulationMode,
                    onToggle = { manager.setSimulationMode(it) }
                )
            }

            // Sunday Availability Check
            if (!isSunday) {
                // RESTRICTION: Not Sunday
                item {
                    NonSundayRestrictionCard(
                        countdownFormatted = manager.formatCountdown(countdownMillis),
                        onBypassRestriction = { manager.setSimulationMode(true) }
                    )
                }
            } else {
                // SUNDAY IS ACTIVE: Show Status, Cooldown, and Selection
                item {
                    SundayStatusTrackerCard(
                        attemptsUsed = attempts.size,
                        remainingAttempts = remainingAttempts,
                        isCooldown = isCooldown,
                        cooldownFormatted = manager.formatCooldownTime(cooldownMillis),
                        onBypassCooldown = { manager.bypassCooldownForTesting() }
                    )
                }

                if (remainingAttempts > 0 && !isCooldown) {
                    // Class Selection Section
                    item {
                        ClassSelectionSection(
                            classes = classes,
                            selectedClassId = selectedClassId,
                            onSelectClass = { selectedClassId = it }
                        )
                    }

                    // Subject Selection Section
                    item {
                        SubjectSelectionSection(
                            subjects = selectedClass.subjects,
                            selectedSubjectId = selectedSubjectId,
                            onSelectSubject = { selectedSubjectId = it }
                        )
                    }

                    // Start Exam CTA Button
                    item {
                        val selectedSubject = selectedClass.subjects.find { it.id == selectedSubjectId }
                        StartExamCtaCard(
                            className = selectedClass.name,
                            subjectName = selectedSubject?.name ?: "Subject",
                            onStartExam = {
                                val subject = selectedClass.subjects.find { it.id == selectedSubjectId }
                                    ?: selectedClass.subjects.first()
                                val paper = SundayTestGenerator.generatePaper(
                                    classId = selectedClass.id,
                                    className = selectedClass.name,
                                    subjectId = subject.id,
                                    subjectName = subject.name
                                )
                                onStartExam(paper)
                            }
                        )
                    }
                } else if (remainingAttempts <= 0) {
                    item {
                        AllAttemptsExhaustedCard(
                            onResetAttempts = { manager.resetAttemptsForTesting() }
                        )
                    }
                }
            }

            // Past Sunday Test Results Section
            if (pastResults.isNotEmpty()) {
                item {
                    PastResultsSection(results = pastResults)
                }
            }
        }
    }
}

@Composable
private fun SundayHeaderBanner(onBackClick: () -> Unit) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = 10.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.12f))
                    .border(1.dp, Color.White.copy(alpha = 0.25f), CircleShape)
                    .testTag("sunday_gate_back_btn")
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Sunday Test Series",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.4.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(AnimeGlowGold.copy(alpha = 0.25f))
                            .border(1.dp, AnimeGlowGold.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 7.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "80 Marks",
                            color = AnimeGlowGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Weekly CBSE standard 3-hour board mock papers",
                    color = Color.White.copy(alpha = 0.75f),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun SimulationToggleCard(
    isSimulationMode: Boolean,
    onToggle: (Boolean) -> Unit
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = 6.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Developer Simulation Mode",
                    color = AnimeGlowCyan,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (isSimulationMode) "Sunday mode active (Exam & Evaluation testable)" else "Checking system device date",
                    color = Color.White.copy(alpha = 0.75f),
                    fontSize = 11.5.sp
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (isSimulationMode) "Simulate Sunday" else "Real Date",
                    color = if (isSimulationMode) AnimeGlowGold else Color.White.copy(alpha = 0.6f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = isSimulationMode,
                    onCheckedChange = onToggle,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = AnimeGlowCyan,
                        uncheckedThumbColor = Color.LightGray,
                        uncheckedTrackColor = Color.White.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.testTag("sunday_simulation_switch")
                )
            }
        }
    }
}

@Composable
private fun NonSundayRestrictionCard(
    countdownFormatted: String,
    onBypassRestriction: () -> Unit = {}
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = 14.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(26.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(listOf(Color(0xFFFF5964), AnimeGlowPink)))
                    .border(2.dp, Color.White.copy(alpha = 0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Lock,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(42.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Exact mandated restriction text:
            Text(
                text = "This test is only available on Sunday. You cannot take the test today.",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp,
                modifier = Modifier.testTag("sunday_restriction_message")
            )

            Spacer(modifier = Modifier.height(18.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.08f))
                    .border(1.dp, AnimeGlowGold.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.AccessTime,
                            contentDescription = null,
                            tint = AnimeGlowGold,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "TIME REMAINING UNTIL SUNDAY",
                            color = AnimeGlowGold,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = countdownFormatted,
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.2.sp,
                        modifier = Modifier.testTag("sunday_countdown_timer")
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "CBSE Board Mock Tests unlock automatically at 00:00 every Sunday.\nTip: Tap below or toggle 'Developer Simulation Mode' above to take the exam anytime during review.",
                color = Color.White.copy(alpha = 0.75f),
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Brush.horizontalGradient(listOf(AnimeGlowPink, AnimeGlowCyan)))
                    .clickable(onClick = onBypassRestriction)
                    .padding(vertical = 12.dp)
                    .testTag("sunday_bypass_restriction_btn"),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.PlayArrow,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "BYPASS & CHOOSE CLASS / SUBJECT",
                        color = Color.White,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun SundayStatusTrackerCard(
    attemptsUsed: Int,
    remainingAttempts: Int,
    isCooldown: Boolean,
    cooldownFormatted: String,
    onBypassCooldown: () -> Unit
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = 10.dp
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF06D6A0))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Sunday Test Portal Active",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(AnimeGlowCyan.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Attempts: $attemptsUsed / ${SundayTestManager.MAX_ATTEMPTS_PER_SUNDAY}",
                        color = AnimeGlowCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Cooldown warning if active
            if (isCooldown) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x33FF9F1C))
                        .border(1.dp, Color(0x77FF9F1C), RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.WarningAmber,
                                contentDescription = null,
                                tint = AnimeGlowGold,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Mandatory 2-Hour Rest Cooldown Active",
                                color = Color.White,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "To prevent mental fatigue, CBSE test policy enforces a 2-hour rest break between attempts.",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 11.5.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Rounded.Timer, contentDescription = null, tint = AnimeGlowGold, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Time Remaining: $cooldownFormatted",
                                    color = AnimeGlowGold,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = "Bypass (Dev)",
                                color = AnimeGlowCyan,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .clickable(onClick = onBypassCooldown)
                                    .padding(4.dp)
                            )
                        }
                    }
                }
            } else {
                Text(
                    text = "You have $remainingAttempts test attempt(s) remaining today. Each test runs strictly for 3 hours with automatic submission upon expiry.",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
private fun ClassSelectionSection(
    classes: List<ClassInfo>,
    selectedClassId: String,
    onSelectClass: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Step 1: Select Your Class",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            classes.forEach { c ->
                val isSelected = c.id == selectedClassId
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            if (isSelected) Brush.horizontalGradient(listOf(AnimeGlowPink, AnimeGlowCyan))
                            else Brush.linearGradient(listOf(Color.White.copy(alpha = 0.10f), Color.White.copy(alpha = 0.05f)))
                        )
                        .border(
                            1.dp,
                            if (isSelected) Color.White else Color.White.copy(alpha = 0.20f),
                            RoundedCornerShape(14.dp)
                        )
                        .clickable { onSelectClass(c.id) }
                        .padding(vertical = 12.dp)
                        .testTag("sunday_class_tab_${c.id}"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = c.name,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun SubjectSelectionSection(
    subjects: List<SubjectInfo>,
    selectedSubjectId: String,
    onSelectSubject: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Step 2: Select Subject",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(subjects, key = { it.id }) { s ->
                val isSelected = s.id == selectedSubjectId
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            if (isSelected) AnimeGlowCyan.copy(alpha = 0.35f)
                            else Color.White.copy(alpha = 0.08f)
                        )
                        .border(
                            1.5.dp,
                            if (isSelected) AnimeGlowCyan else Color.White.copy(alpha = 0.18f),
                            RoundedCornerShape(14.dp)
                        )
                        .clickable { onSelectSubject(s.id) }
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .testTag("sunday_subject_tab_${s.id}")
                ) {
                    Text(
                        text = s.name,
                        color = if (isSelected) Color.White else Color.White.copy(alpha = 0.8f),
                        fontSize = 13.5.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun StartExamCtaCard(
    className: String,
    subjectName: String,
    onStartExam: () -> Unit
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = 12.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "$className • $subjectName",
                        color = AnimeGlowGold,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "CBSE Board Mock Exam (80 Marks)",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(AnimeGlowPink, AnimeGlowCyan))),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Quiz,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Specs badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SpecBadge(text = "38 Questions", modifier = Modifier.weight(1f))
                SpecBadge(text = "3 Hours (180m)", modifier = Modifier.weight(1f))
                SpecBadge(text = "Pass: 60+ Marks", modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Launch button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Brush.horizontalGradient(listOf(AnimeGlowPink, AnimeGlowCyan)))
                    .clickable(onClick = onStartExam)
                    .padding(vertical = 15.dp)
                    .testTag("start_sunday_exam_btn"),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.PlayArrow, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "START SUNDAY EXAM",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.8.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun SpecBadge(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White.copy(alpha = 0.08f))
            .border(1.dp, Color.White.copy(alpha = 0.16f), RoundedCornerShape(10.dp))
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White.copy(alpha = 0.9f),
            fontSize = 11.5.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun AllAttemptsExhaustedCard(
    onResetAttempts: () -> Unit = {}
) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Rounded.CheckCircle, contentDescription = null, tint = AnimeGlowGold, modifier = Modifier.size(36.dp))
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "All 3 Attempts Completed",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "You have exhausted your 3 Sunday test attempts for today. Next Sunday test series opens next Sunday at 00:00.",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(AnimeGlowCyan.copy(alpha = 0.2f))
                    .border(1.dp, AnimeGlowCyan.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                    .clickable(onClick = onResetAttempts)
                    .padding(horizontal = 14.dp, vertical = 8.dp)
                    .testTag("sunday_reset_attempts_btn")
            ) {
                Text(
                    text = "Reset Attempts (Dev Mode)",
                    color = AnimeGlowCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun PastResultsSection(results: List<com.example.model.SundayTestResult>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Your Sunday Test History",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))

        results.take(3).forEach { r ->
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
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
                            text = "${r.className} • ${r.subjectName}",
                            color = Color.White,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Score: ${r.score}/80 (${r.percentage.toInt()}%)",
                            color = if (r.isPassed) Color(0xFF06D6A0) else Color(0xFFFF8A8A),
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (r.isPassed) Color(0x3306D6A0) else Color(0x33E63946))
                            .border(1.dp, if (r.isPassed) Color(0xFF06D6A0) else Color(0xFFFF6B6B), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = if (r.isPassed) "PASSED" else "NEEDS REVISION",
                            color = if (r.isPassed) Color(0xFF06D6A0) else Color(0xFFFF6B6B),
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }
    }
}
