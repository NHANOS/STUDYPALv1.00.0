package com.example.ui.screens.sundaytest

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.HourglassBottom
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material.icons.rounded.Rule
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.QuestionEvaluationResult
import com.example.model.SundayTestResult
import com.example.ui.components.AnimeGlowCyan
import com.example.ui.components.AnimeGlowGold
import com.example.ui.components.AnimeGlowPink
import com.example.ui.components.GlassCard

@Composable
fun SundayEvaluationLoadingScreen(
    currentLayer: Int,
    statusMessage: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        GlassCard(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .widthIn(max = 480.dp),
            elevation = 16.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(AnimeGlowPink, AnimeGlowCyan))),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(42.dp),
                        strokeWidth = 3.5.dp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Two-Layer Evaluation Pipeline",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Stepper Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    EvaluationStepChip(step = 1, title = "Internal AI", isActive = currentLayer == 1, isCompleted = currentLayer > 1)
                    Spacer(modifier = Modifier.width(10.dp))
                    Box(modifier = Modifier.width(20.dp).height(1.dp).background(Color.White.copy(0.3f)))
                    Spacer(modifier = Modifier.width(10.dp))
                    EvaluationStepChip(step = 2, title = "Grounded Web AI", isActive = currentLayer == 2, isCompleted = false)
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = statusMessage,
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                LinearProgressIndicator(
                    progress = { if (currentLayer == 1) 0.5f else 0.9f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = AnimeGlowCyan,
                    trackColor = Color.White.copy(alpha = 0.15f)
                )
            }
        }
    }
}

@Composable
private fun EvaluationStepChip(step: Int, title: String, isActive: Boolean, isCompleted: Boolean) {
    val bg = when {
        isCompleted -> Color(0xFF06D6A0)
        isActive -> AnimeGlowCyan
        else -> Color.White.copy(alpha = 0.15f)
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(bg.copy(alpha = 0.25f))
            .border(1.dp, bg, RoundedCornerShape(10.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = "L$step: $title",
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun SundayResultScreen(
    result: SundayTestResult,
    onBackToPortal: () -> Unit,
    onBackToHome: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
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
            // Main Score Banner
            item {
                ScoreBannerCard(result = result)
            }

            // Section Score Breakdown
            item {
                SectionBreakdownCard(result = result)
            }

            // Two-Layer AI Audit Summaries
            item {
                AiAuditSummariesCard(result = result)
            }

            // Section Header for Detailed Question Review
            item {
                Text(
                    text = "Question-by-Question Verified Review (${result.questionResults.size} Questions)",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Detailed question review cards
            items(result.questionResults, key = { it.questionNumber }) { qRes ->
                DetailedQuestionReviewCard(res = qRes)
            }

            // Action Buttons
            item {
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.White.copy(alpha = 0.12f))
                            .border(1.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
                            .clickable(onClick = onBackToHome)
                            .padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Back to Home",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Brush.horizontalGradient(listOf(AnimeGlowPink, AnimeGlowCyan)))
                            .clickable(onClick = onBackToPortal)
                            .padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Sunday Portal",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ScoreBannerCard(result: SundayTestResult) {
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
                        if (result.isPassed) Brush.linearGradient(listOf(AnimeGlowCyan, Color(0xFF06D6A0)))
                        else Brush.linearGradient(listOf(Color(0xFFFF5964), AnimeGlowPink))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (result.isPassed) Icons.Rounded.EmojiEvents else Icons.Rounded.Cancel,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(38.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = if (result.isPassed) "CONGRATULATIONS! BENCHMARK ACHIEVED" else "ATTEMPT COMPLETED",
                color = if (result.isPassed) AnimeGlowGold else Color(0xFFFF6B6B),
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${result.score} / ${result.totalMarks} Marks",
                color = Color.White,
                fontSize = 34.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.testTag("sunday_exam_final_score")
            )

            Text(
                text = "Score: ${result.percentage.toInt()}% • Passing Benchmark: 60+ Marks (75%)",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 12.5.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Cooldown & persistence confirmation
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (result.isPassed) Color(0x3306D6A0) else Color(0x33FF9F1C))
                    .border(1.dp, if (result.isPassed) Color(0x7706D6A0) else Color(0x77FF9F1C), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (result.isPassed) Icons.Rounded.CheckCircle else Icons.Rounded.HourglassBottom,
                            contentDescription = null,
                            tint = if (result.isPassed) Color(0xFF06D6A0) else AnimeGlowGold,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (result.isPassed) "Results Saved to Academic Profile" else "2-Hour Rest Cooldown Active",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = if (result.isPassed)
                            "Passed 60+ benchmark! Verified scores updated in local storage and progress metrics."
                        else
                            "CBSE exam rules mandate a 2-hour rest break before taking your next attempt.",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 11.5.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionBreakdownCard(result: SundayTestResult) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Section-Wise Performance",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))

            result.sectionScores.forEach { (secName, obtained) ->
                val max = result.sectionMaxMarks[secName] ?: 1
                val progress = (obtained.toFloat() / max.toFloat()).coerceIn(0f, 1f)

                Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = secName,
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "$obtained / $max Marks",
                            color = AnimeGlowCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = if (progress >= 0.75f) Color(0xFF06D6A0) else if (progress >= 0.5f) AnimeGlowGold else Color(0xFFFF5964),
                        trackColor = Color.White.copy(alpha = 0.12f)
                    )
                }
            }
        }
    }
}

@Composable
private fun AiAuditSummariesCard(result: SundayTestResult) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Layer 1 Box
            Row(verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(AnimeGlowPink.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Rounded.Rule, contentDescription = null, tint = AnimeGlowPink, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Layer 1: Internal AI Verification",
                        color = AnimeGlowPink,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = result.overallLayer1Summary,
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }

            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.White.copy(0.12f)))

            // Layer 2 Box
            Row(verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(AnimeGlowCyan.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Rounded.Public, contentDescription = null, tint = AnimeGlowCyan, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Layer 2: Web-Grounded Verification",
                        color = AnimeGlowCyan,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = result.overallLayer2Summary,
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailedQuestionReviewCard(res: QuestionEvaluationResult) {
    var isExpanded by remember { mutableStateOf(false) }
    val isFullCredit = res.marksAwarded == res.maxMarks
    val isZero = res.marksAwarded == 0

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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Q${res.questionNumber}",
                        color = AnimeGlowGold,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "• ${res.section.title}",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 11.5.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (isFullCredit) Color(0x3306D6A0)
                                else if (!isZero) Color(0x33FFD166)
                                else Color(0x33E63946)
                            )
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "${res.marksAwarded} / ${res.maxMarks} Marks",
                            color = if (isFullCredit) Color(0xFF06D6A0) else if (!isZero) AnimeGlowGold else Color(0xFFFF6B6B),
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    IconButton(
                        onClick = { isExpanded = !isExpanded },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = if (isExpanded) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                            contentDescription = "Expand details",
                            tint = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = res.questionText,
                color = Color.White,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 18.sp
            )

            // Collapsible details
            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Candidate Answer
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White.copy(alpha = 0.06f))
                            .padding(10.dp)
                    ) {
                        Column {
                            Text(text = "Your Submitted Answer:", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(text = res.userResponseText, color = Color.White, fontSize = 12.sp)
                        }
                    }

                    // Model Answer
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(AnimeGlowCyan.copy(alpha = 0.08f))
                            .border(1.dp, AnimeGlowCyan.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                            .padding(10.dp)
                    ) {
                        Column {
                            Text(text = "CBSE Model Solution / Marking Key:", color = AnimeGlowCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(text = res.modelAnswerText, color = Color.White, fontSize = 12.sp, lineHeight = 16.sp)
                        }
                    }

                    // Layer 1 Rubric Note
                    Text(
                        text = res.layer1Feedback,
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 11.5.sp
                    )

                    // Layer 2 Grounding Note
                    Text(
                        text = res.layer2WebGroundedFeedback,
                        color = AnimeGlowCyan.copy(alpha = 0.9f),
                        fontSize = 11.5.sp
                    )
                }
            }
        }
    }
}
