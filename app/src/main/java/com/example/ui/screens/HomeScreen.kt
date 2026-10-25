package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.R
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.EditNote
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.Psychology
import androidx.compose.material.icons.rounded.Quiz
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.Stars
import androidx.compose.material.icons.rounded.WorkspacePremium
import androidx.compose.material3.Icon
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
import com.example.model.StudyRepository
import com.example.ui.background.AnimeBackgroundTheme
import com.example.ui.components.AnimeGlowCyan
import com.example.ui.components.AnimeGlowGold
import com.example.ui.components.AnimeGlowPink
import com.example.ui.components.ComingSoonBadge
import com.example.ui.components.GlassCard

data class FeatureCardItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val accentColors: List<Color>
)

@Composable
fun HomeScreen(
    currentTheme: AnimeBackgroundTheme,
    onClassSelected: (String) -> Unit,
    onMovieZoneClick: () -> Unit,
    onStudyClick: () -> Unit,
    onStudyXClick: () -> Unit,
    onNotesClick: () -> Unit = {},
    onChatAiClick: () -> Unit = {},
    onQuestionSolvingClick: () -> Unit = {},
    onFeatureClick: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val classes = StudyRepository.classes

    val featureList = listOf(
        FeatureCardItem(
            id = "feature_study",
            title = "Study",
            subtitle = "Focused study sessions, Pomodoro intervals & syllabus tracker",
            icon = Icons.Rounded.MenuBook,
            accentColors = listOf(Color(0xFFFF7597), Color(0xFFFF9B71))
        ),
        FeatureCardItem(
            id = "feature_study_x",
            title = "STUDYPAL",
            subtitle = "Flagship smart learning suite with deep focus algorithms",
            icon = Icons.Rounded.WorkspacePremium,
            accentColors = listOf(Color(0xFF8A4FFF), Color(0xFFC77DFF))
        ),
        FeatureCardItem(
            id = "feature_movie_zone",
            title = "Movie Zone",
            subtitle = "Concept animated films, documentaries & visual learning stories",
            icon = Icons.Rounded.Movie,
            accentColors = listOf(Color(0xFFFFD166), Color(0xFFFF9F1C))
        ),
        FeatureCardItem(
            id = "feature_notes",
            title = "Notes",
            subtitle = "Handwritten revision notes, quick summaries & formula sheets",
            icon = Icons.Rounded.EditNote,
            accentColors = listOf(Color(0xFF06D6A0), Color(0xFF1B9AAA))
        ),
        FeatureCardItem(
            id = "feature_chat_ai",
            title = "Chat AI",
            subtitle = "24/7 intelligent study tutor for instant doubt clarification",
            icon = Icons.Rounded.Psychology,
            accentColors = listOf(Color(0xFF64D2FF), Color(0xFF3A86FF))
        ),
        FeatureCardItem(
            id = "feature_question_solving",
            title = "Question Solving",
            subtitle = "Step-by-step problem solver, practice questions & mock papers",
            icon = Icons.Rounded.Quiz,
            accentColors = listOf(Color(0xFFFF5964), Color(0xFFEE6C4D))
        )
    )

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 680.dp),
            contentPadding = PaddingValues(
                top = contentPadding.calculateTopPadding() + 12.dp,
                bottom = contentPadding.calculateBottomPadding() + 24.dp,
                start = 16.dp,
                end = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Welcome Header Glass Banner
            item {
                WelcomeHeader(
                    currentTheme = currentTheme
                )
            }

            // Section 1: Four Class Cards in 2x2 Grid
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Select Your Class",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "4 Grades Available",
                            color = Color.White.copy(alpha = 0.70f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 2 x 2 Class Grid
                    ClassGrid2x2(
                        classes = classes,
                        onClassSelected = onClassSelected
                    )
                }
            }

            // Section 2: Six Feature Cards in a Single-Column Layout
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Study Ecosystem",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "Live Modules",
                        color = AnimeGlowGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            items(featureList, key = { it.id }) { feature ->
                FeatureCardRow(
                    item = feature,
                    onClick = {
                        when (feature.id) {
                            "feature_study" -> onStudyClick()
                            "feature_study_x" -> onStudyXClick()
                            "feature_movie_zone" -> onMovieZoneClick()
                            "feature_notes" -> onNotesClick()
                            "feature_chat_ai" -> onChatAiClick()
                            "feature_question_solving" -> onQuestionSolvingClick()
                            else -> onFeatureClick(
                                feature.title,
                                "The ${feature.title} module is currently under active development and will be unlocked in an upcoming release of STUDYPAL."
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun WelcomeHeader(
    currentTheme: AnimeBackgroundTheme
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
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "STUDYPAL Academy",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Elevate your learning with anime focus aesthetics",
                        color = Color.White.copy(alpha = 0.80f),
                        fontSize = 13.sp
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.ic_app_logo),
                    contentDescription = "STUDYPAL Logo",
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.35f), RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Fixed Session Background Theme indicator
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.10f))
                    .border(1.dp, Color.White.copy(alpha = 0.20f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(AnimeGlowGold)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Session Theme: ${currentTheme.displayName}",
                    color = Color.White.copy(alpha = 0.90f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun ClassGrid2x2(
    classes: List<ClassInfo>,
    onClassSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Row 1: Class 9 and Class 10
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val class9 = classes.find { it.id == "class_9" }
            val class10 = classes.find { it.id == "class_10" }

            if (class9 != null) {
                ClassGridCard(
                    classInfo = class9,
                    gradientColors = listOf(Color(0xFFFF7597), Color(0xFFFF9E7A)),
                    testTag = "class_card_9",
                    modifier = Modifier.weight(1f),
                    onClick = { onClassSelected(class9.id) }
                )
            }
            if (class10 != null) {
                ClassGridCard(
                    classInfo = class10,
                    gradientColors = listOf(Color(0xFF64D2FF), Color(0xFF5E5CE6)),
                    testTag = "class_card_10",
                    modifier = Modifier.weight(1f),
                    onClick = { onClassSelected(class10.id) }
                )
            }
        }

        // Row 2: Class 11 and Class 12
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val class11 = classes.find { it.id == "class_11" }
            val class12 = classes.find { it.id == "class_12" }

            if (class11 != null) {
                ClassGridCard(
                    classInfo = class11,
                    gradientColors = listOf(Color(0xFF06D6A0), Color(0xFF1B9AAA)),
                    testTag = "class_card_11",
                    modifier = Modifier.weight(1f),
                    onClick = { onClassSelected(class11.id) }
                )
            }
            if (class12 != null) {
                ClassGridCard(
                    classInfo = class12,
                    gradientColors = listOf(Color(0xFFFFD166), Color(0xFFFF7597)),
                    testTag = "class_card_12",
                    modifier = Modifier.weight(1f),
                    onClick = { onClassSelected(class12.id) }
                )
            }
        }
    }
}

@Composable
private fun ClassGridCard(
    classInfo: ClassInfo,
    gradientColors: List<Color>,
    testTag: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    GlassCard(
        modifier = modifier.height(138.dp),
        onClick = onClick,
        testTag = testTag,
        elevation = 10.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Brush.linearGradient(gradientColors)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.School,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Interactive arrow
                Icon(
                    imageVector = Icons.Rounded.ChevronRight,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.70f),
                    modifier = Modifier.size(20.dp)
                )
            }

            Column {
                Text(
                    text = classInfo.name,
                    color = Color.White,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.4.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${classInfo.subjects.size} Configured Subjects",
                    color = Color.White.copy(alpha = 0.75f),
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun FeatureCardRow(
    item: FeatureCardItem,
    onClick: () -> Unit
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        testTag = item.id,
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
                    .background(Brush.linearGradient(item.accentColors)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
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
                        text = item.title,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    val badgeText = when (item.id) {
                        "feature_study", "feature_study_x", "feature_notes", "feature_question_solving" -> "In-App Web"
                        "feature_movie_zone" -> "Open"
                        "feature_chat_ai" -> "ChatGPT"
                        else -> "Live"
                    }
                    ComingSoonBadge(text = badgeText)
                }

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = item.subtitle,
                    color = Color.White.copy(alpha = 0.72f),
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

