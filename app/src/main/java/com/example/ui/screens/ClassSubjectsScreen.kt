package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material.icons.rounded.Biotech
import androidx.compose.material.icons.rounded.Calculate
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.Gavel
import androidx.compose.material.icons.rounded.HistoryEdu
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material.icons.rounded.Science
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ClassInfo
import com.example.model.SubjectInfo
import com.example.ui.components.AnimeGlowCyan
import com.example.ui.components.AnimeGlowGold
import com.example.ui.components.AnimeGlowPink
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.StudyLockManager
import com.example.ui.components.GlassCard

@Composable
fun ClassSubjectsScreen(
    classInfo: ClassInfo,
    onBackClick: () -> Unit,
    onSubjectClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val context = LocalContext.current
    val studyLockManager = remember { StudyLockManager.getInstance(context) }
    val classLocks by studyLockManager.activeClassLocks.collectAsStateWithLifecycle()
    val activeLock = classLocks[classInfo.id]

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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header with Back button and Class Info
            item {
                ClassHeaderBanner(
                    classInfo = classInfo,
                    onBackClick = onBackClick
                )
            }

            // Active Class Lock Banner
            if (activeLock != null) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0x33FFB703))
                            .border(1.dp, Color(0x77FFB703), RoundedCornerShape(14.dp))
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Lock,
                            contentDescription = null,
                            tint = AnimeGlowGold,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Focus Lock in Progress",
                                color = AnimeGlowGold,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Incomplete chapter '${activeLock.chapterTitle}' is active. Complete study timer and score 60+ on the 80-mark test to unlock other chapters.",
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 11.5.sp
                            )
                        }
                    }
                }
            }

            // Sample curriculum disclaimer note
            item {
                CurriculumSampleDisclaimer()
            }

            // Subject Cards List
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Curriculum Subjects",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${classInfo.subjects.size} Subjects Available",
                        color = AnimeGlowGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            items(classInfo.subjects, key = { it.id }) { subject ->
                SubjectCard(
                    subject = subject,
                    onClick = { onSubjectClick(subject.id) }
                )
            }
        }
    }
}

@Composable
private fun ClassHeaderBanner(
    classInfo: ClassInfo,
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
                // Back button
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.12f))
                        .border(1.dp, Color.White.copy(alpha = 0.25f), CircleShape)
                        .testTag("class_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back to Classes",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = classInfo.name,
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = classInfo.subtitle,
                        color = Color.White.copy(alpha = 0.78f),
                        fontSize = 12.5.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun CurriculumSampleDisclaimer() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White.copy(alpha = 0.08f))
            .border(1.dp, Color.White.copy(alpha = 0.16f), RoundedCornerShape(14.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(AnimeGlowGold)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "Configured Sample Curriculum: Subject syllabi are structured for preview and can be customized per school board.",
            color = Color.White.copy(alpha = 0.82f),
            fontSize = 11.5.sp,
            lineHeight = 15.sp
        )
    }
}

@Composable
private fun SubjectCard(
    subject: SubjectInfo,
    onClick: () -> Unit
) {
    val (icon, gradientColors) = getSubjectVisuals(subject.iconCategory)

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        testTag = "subject_card_${subject.id}",
        elevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Brush.linearGradient(gradientColors)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = subject.name,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = subject.name,
                        color = Color.White,
                        fontSize = 16.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = subject.code,
                        color = AnimeGlowCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = subject.description,
                    color = Color.White.copy(alpha = 0.72f),
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${subject.chapters.size} Configured Chapters",
                    color = AnimeGlowGold,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.Rounded.ChevronRight,
                contentDescription = "View Chapters",
                tint = Color.White.copy(alpha = 0.75f),
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

private fun getSubjectVisuals(category: String): Pair<ImageVector, List<Color>> {
    return when (category) {
        "math" -> Pair(Icons.Rounded.Calculate, listOf(Color(0xFF8A4FFF), Color(0xFFC77DFF)))
        "physics" -> Pair(Icons.Rounded.Science, listOf(Color(0xFF3A86FF), Color(0xFF64D2FF)))
        "chemistry" -> Pair(Icons.Rounded.Biotech, listOf(Color(0xFFFF5964), Color(0xFFFF9B71)))
        "biology" -> Pair(Icons.Rounded.Biotech, listOf(Color(0xFF06D6A0), Color(0xFF1B9AAA)))
        "english" -> Pair(Icons.AutoMirrored.Rounded.MenuBook, listOf(Color(0xFFFF7597), Color(0xFFFFB7C5)))
        "history" -> Pair(Icons.Rounded.HistoryEdu, listOf(Color(0xFFFF9F1C), Color(0xFFFFD166)))
        "geography" -> Pair(Icons.Rounded.Public, listOf(Color(0xFF2EC4B6), Color(0xFFCBF3F0)))
        "civics" -> Pair(Icons.Rounded.Gavel, listOf(Color(0xFF9D4EDD), Color(0xFFE0AAFF)))
        "economics" -> Pair(Icons.AutoMirrored.Rounded.TrendingUp, listOf(Color(0xFF2A9D8F), Color(0xFFE76F51)))
        "cs" -> Pair(Icons.Rounded.Code, listOf(Color(0xFF00B4D8), Color(0xFF90E0EF)))
        else -> Pair(Icons.AutoMirrored.Rounded.MenuBook, listOf(AnimeGlowPink, AnimeGlowCyan))
    }
}
