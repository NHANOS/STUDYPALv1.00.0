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
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.StudyLockManager
import com.example.model.ChapterInfo
import com.example.model.ClassInfo
import com.example.model.SubjectInfo
import com.example.ui.components.AnimeGlowCyan
import com.example.ui.components.AnimeGlowGold
import com.example.ui.components.AnimeGlowPink
import com.example.ui.components.GlassCard

@Composable
fun SubjectChaptersScreen(
    classInfo: ClassInfo,
    subjectInfo: SubjectInfo,
    onBackClick: () -> Unit,
    onChapterClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val context = LocalContext.current
    val studyLockManager = remember { StudyLockManager.getInstance(context) }
    val classLocks by studyLockManager.activeClassLocks.collectAsStateWithLifecycle()
    val completedChapters by studyLockManager.completedChapters.collectAsStateWithLifecycle()

    val currentClassLock = classLocks[classInfo.id]
    var lockedDialogChapterTitle by remember { mutableStateOf<String?>(null) }

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
            // Header with Back button, Class and Subject details
            item {
                SubjectHeaderBanner(
                    classInfo = classInfo,
                    subjectInfo = subjectInfo,
                    onBackClick = onBackClick
                )
            }

            // Class-Wide Lock Banner if active
            if (currentClassLock != null) {
                item {
                    ClassLockWarningBanner(
                        className = classInfo.name,
                        activeChapterTitle = currentClassLock.chapterTitle,
                        onGoToActiveChapter = {
                            onChapterClick(currentClassLock.chapterId)
                        }
                    )
                }
            }

            // Chapter List Title
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Chapters & Topics",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${subjectInfo.chapters.size} Chapters Ready",
                        color = AnimeGlowGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Chapter Cards
            items(subjectInfo.chapters, key = { it.id }) { chapter ->
                val isLocked = studyLockManager.isChapterLocked(classInfo.id, chapter.id)
                val completionRecord = completedChapters[chapter.id]
                val isCompleted = completionRecord?.isPassed == true
                val isActiveChapter = currentClassLock?.chapterId == chapter.id

                ChapterCard(
                    chapter = chapter,
                    isLocked = isLocked,
                    isCompleted = isCompleted,
                    isActiveChapter = isActiveChapter,
                    score = completionRecord?.score,
                    activeLockChapterTitle = currentClassLock?.chapterTitle,
                    onClick = {
                        if (isLocked) {
                            lockedDialogChapterTitle = chapter.title
                        } else {
                            onChapterClick(chapter.id)
                        }
                    }
                )
            }
        }
    }

    // Locked Chapter Information Dialog
    if (lockedDialogChapterTitle != null && currentClassLock != null) {
        AlertDialog(
            onDismissRequest = { lockedDialogChapterTitle = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.Lock, contentDescription = null, tint = AnimeGlowGold, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Chapter Locked", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column {
                    Text(
                        text = "All other chapters in ${classInfo.name} are locked because you have an active study session on:",
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "• ${currentClassLock.chapterTitle}",
                        color = AnimeGlowCyan,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "To unlock all chapters in ${classInfo.name}, complete the active study timer and score 60+ marks on its 80-mark chapter test.",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.5.sp
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        lockedDialogChapterTitle = null
                        onChapterClick(currentClassLock.chapterId)
                    }
                ) {
                    Text("Go to Active Chapter", color = AnimeGlowGold, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { lockedDialogChapterTitle = null }) {
                    Text("Close", color = Color.White.copy(alpha = 0.7f))
                }
            },
            containerColor = Color(0xFF1E243A)
        )
    }
}

@Composable
private fun ClassLockWarningBanner(
    className: String,
    activeChapterTitle: String,
    onGoToActiveChapter: () -> Unit
) {
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
                text = "Class-Wide Lock Active ($className)",
                color = AnimeGlowGold,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Active on '$activeChapterTitle'. Complete test with 60+ marks to unlock.",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 11.5.sp
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(AnimeGlowGold.copy(alpha = 0.25f))
                .clickable(onClick = onGoToActiveChapter)
                .padding(horizontal = 8.dp, vertical = 5.dp)
        ) {
            Text(
                text = "Resume",
                color = AnimeGlowGold,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun SubjectHeaderBanner(
    classInfo: ClassInfo,
    subjectInfo: SubjectInfo,
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
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.12f))
                        .border(1.dp, Color.White.copy(alpha = 0.25f), CircleShape)
                        .testTag("chapter_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back to Subjects",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = classInfo.name,
                            color = AnimeGlowCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "•",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = subjectInfo.code,
                            color = Color.White.copy(alpha = 0.75f),
                            fontSize = 12.sp
                        )
                    }
                    Text(
                        text = subjectInfo.name,
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = subjectInfo.description,
                color = Color.White.copy(alpha = 0.80f),
                fontSize = 12.5.sp,
                lineHeight = 17.sp
            )
        }
    }
}

@Composable
private fun ChapterCard(
    chapter: ChapterInfo,
    isLocked: Boolean,
    isCompleted: Boolean,
    isActiveChapter: Boolean,
    score: Int?,
    activeLockChapterTitle: String?,
    onClick: () -> Unit
) {
    val cardBackgroundAlpha = if (isLocked) 0.05f else 0.12f

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        testTag = "chapter_card_${chapter.id}",
        elevation = if (isLocked) 4.dp else 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Chapter Number Badge or Lock Icon
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        when {
                            isLocked -> Brush.linearGradient(listOf(Color(0x33FFFFFF), Color(0x1AFFFFFF)))
                            isCompleted -> Brush.linearGradient(listOf(Color(0xFF06D6A0), AnimeGlowCyan))
                            isActiveChapter -> Brush.linearGradient(listOf(AnimeGlowGold, AnimeGlowPink))
                            else -> Brush.linearGradient(listOf(AnimeGlowPink, AnimeGlowCyan))
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isLocked) {
                    Icon(
                        imageVector = Icons.Rounded.Lock,
                        contentDescription = "Locked Chapter",
                        tint = AnimeGlowGold,
                        modifier = Modifier.size(22.dp)
                    )
                } else if (isCompleted) {
                    Icon(
                        imageVector = Icons.Rounded.CheckCircle,
                        contentDescription = "Completed Chapter",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = "${chapter.number}",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Chapter ${chapter.number}: ${chapter.title}",
                        color = if (isLocked) Color.White.copy(alpha = 0.6f) else Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )

                    if (isCompleted) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0x3306D6A0))
                                .border(1.dp, Color(0xFF06D6A0), RoundedCornerShape(8.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Passed ${score ?: 60}/80",
                                color = Color(0xFF06D6A0),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else if (isActiveChapter) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(AnimeGlowGold.copy(alpha = 0.25f))
                                .border(1.dp, AnimeGlowGold, RoundedCornerShape(8.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "IN FOCUS",
                                color = AnimeGlowGold,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else if (isLocked) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0x33FFB703))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "LOCKED",
                                color = AnimeGlowGold,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (isLocked) "Locked by active study session on '$activeLockChapterTitle'" else chapter.summary,
                    color = if (isLocked) AnimeGlowGold.copy(alpha = 0.8f) else Color.White.copy(alpha = 0.72f),
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.Schedule,
                        contentDescription = null,
                        tint = if (isLocked) Color.White.copy(alpha = 0.4f) else AnimeGlowGold,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "~${chapter.estimatedMinutes} mins",
                        color = if (isLocked) Color.White.copy(alpha = 0.4f) else AnimeGlowGold,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Icon(
                        imageVector = Icons.Rounded.AutoAwesome,
                        contentDescription = null,
                        tint = if (isLocked) Color.White.copy(alpha = 0.4f) else AnimeGlowCyan,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${chapter.highlights.size} Highlights",
                        color = if (isLocked) Color.White.copy(alpha = 0.4f) else AnimeGlowCyan,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = if (isLocked) Icons.Rounded.Lock else Icons.Rounded.ChevronRight,
                contentDescription = if (isLocked) "Locked" else "Study Chapter",
                tint = if (isLocked) AnimeGlowGold else Color.White.copy(alpha = 0.75f),
                modifier = Modifier.size(22.dp)
            )
        }
    }
}
