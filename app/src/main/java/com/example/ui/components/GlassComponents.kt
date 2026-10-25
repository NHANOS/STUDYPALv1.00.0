package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.example.data.UserProfile
import java.io.File
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Assessment
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Quiz
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Stars
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R

// Glassmorphism color palette constants
val GlassBackgroundDeep = Color(0x380F1426)
val GlassBackgroundLight = Color(0x2EFFFFFF)
val GlassBackgroundCard = Color(0x28FFFFFF)
val GlassBorderTop = Color(0x60FFFFFF)
val GlassBorderBottom = Color(0x18FFFFFF)
val AnimeGlowPink = Color(0xFFFF7597)
val AnimeGlowCyan = Color(0xFF64D2FF)
val AnimeGlowGold = Color(0xFFFFD166)

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(22.dp),
    backgroundColor: Color = GlassBackgroundCard,
    borderColor: Color = GlassBorderTop,
    borderWidth: Dp = 1.dp,
    elevation: Dp = 8.dp,
    onClick: (() -> Unit)? = null,
    testTag: String? = null,
    content: @Composable () -> Unit
) {
    val clickableModifier = if (onClick != null) {
        Modifier.clickable(
            onClick = onClick
        )
    } else Modifier

    val tagModifier = if (testTag != null) Modifier.testTag(testTag) else Modifier

    Surface(
        modifier = modifier
            .then(tagModifier)
            .shadow(elevation = elevation, shape = shape, spotColor = Color(0x40000000))
            .then(clickableModifier),
        shape = shape,
        color = backgroundColor,
        border = BorderStroke(
            borderWidth,
            Brush.verticalGradient(
                colors = listOf(borderColor, GlassBorderBottom)
            )
        )
    ) {
        Box(
            modifier = Modifier.background(
                Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.12f),
                        Color.Transparent,
                        Color.Black.copy(alpha = 0.08f)
                    )
                )
            )
        ) {
            content()
        }
    }
}

@Composable
fun ComingSoonBadge(
    text: String = "Coming Soon",
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        AnimeGlowPink.copy(alpha = 0.35f),
                        AnimeGlowCyan.copy(alpha = 0.25f)
                    )
                )
            )
            .border(
                1.dp,
                Brush.horizontalGradient(
                    listOf(
                        AnimeGlowPink.copy(alpha = 0.7f),
                        AnimeGlowCyan.copy(alpha = 0.6f)
                    )
                ),
                RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 9.dp, vertical = 3.5.dp)
    ) {
        Icon(
            imageVector = Icons.Rounded.AutoAwesome,
            contentDescription = null,
            tint = AnimeGlowGold,
            modifier = Modifier.size(12.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.5.sp
        )
    }
}

/**
 * Fixed Glassmorphism Top Navigation Bar
 */
@Composable
fun GlassTopBar(
    title: String = "STUDYPAL",
    userProfile: UserProfile? = null,
    onMenuClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp), spotColor = Color(0x50000000))
            .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0x750D111F),
                        Color(0x50151C33)
                    )
                )
            )
            .border(
                BorderStroke(
                    1.dp,
                    Brush.verticalGradient(
                        colors = listOf(Color.White.copy(0.35f), Color.White.copy(0.12f))
                    )
                ),
                shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
            )
            .padding(top = statusBarHeight, bottom = 10.dp, start = 14.dp, end = 14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Hamburger menu button
            IconButton(
                onClick = onMenuClick,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.12f))
                    .border(1.dp, Color.White.copy(alpha = 0.25f), CircleShape)
                    .testTag("hamburger_menu_btn")
            ) {
                Icon(
                    imageVector = Icons.Rounded.Menu,
                    contentDescription = "Open navigation menu",
                    tint = Color.White
                )
            }

            // Center App Name with Brand Logo & Glow
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_app_logo),
                    contentDescription = "STUDYPAL Logo",
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.35f), RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.8.sp
                )
            }

            // Right side synchronized profile button
            val hasCustomImage = userProfile?.imagePath != null && File(userProfile.imagePath).exists()

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.12f))
                    .border(
                        1.5.dp,
                        Brush.linearGradient(
                            listOf(AnimeGlowPink.copy(alpha = 0.85f), AnimeGlowCyan.copy(alpha = 0.85f))
                        ),
                        CircleShape
                    )
                    .clickable(onClick = onProfileClick)
                    .testTag("top_bar_profile_btn"),
                contentAlignment = Alignment.Center
            ) {
                if (hasCustomImage) {
                    AsyncImage(
                        model = File(userProfile!!.imagePath!!),
                        contentDescription = "Profile",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(AnimeGlowPink, AnimeGlowCyan)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userProfile?.displayInitial ?: "S",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

/**
 * Fixed Glassmorphism Bottom Navigation Bar
 */
@Composable
fun GlassBottomBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val navBarBottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .padding(bottom = navBarBottom)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(16.dp, RoundedCornerShape(26.dp), spotColor = Color(0x60000000)),
            shape = RoundedCornerShape(26.dp),
            color = Color(0x650D111F),
            border = BorderStroke(
                1.dp,
                Brush.verticalGradient(
                    listOf(Color.White.copy(0.40f), Color.White.copy(0.12f))
                )
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomNavItem(
                    title = stringResource(R.string.nav_home),
                    icon = Icons.Rounded.Home,
                    isSelected = selectedTab == 0,
                    testTag = "nav_tab_home",
                    onClick = { onTabSelected(0) }
                )

                BottomNavItem(
                    title = stringResource(R.string.nav_sunday_test),
                    icon = Icons.Rounded.Quiz,
                    isSelected = selectedTab == 1,
                    testTag = "nav_tab_sunday_test",
                    onClick = { onTabSelected(1) }
                )

                BottomNavItem(
                    title = stringResource(R.string.nav_progress),
                    icon = Icons.Rounded.Assessment,
                    isSelected = selectedTab == 2,
                    testTag = "nav_tab_progress",
                    onClick = { onTabSelected(2) }
                )
            }
        }
    }
}

@Composable
private fun BottomNavItem(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    testTag: String,
    onClick: () -> Unit
) {
    val backgroundModifier = if (isSelected) {
        Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        AnimeGlowPink.copy(alpha = 0.35f),
                        AnimeGlowCyan.copy(alpha = 0.30f)
                    )
                )
            )
            .border(
                1.dp,
                Brush.horizontalGradient(
                    listOf(AnimeGlowPink.copy(0.7f), AnimeGlowCyan.copy(0.6f))
                ),
                RoundedCornerShape(18.dp)
            )
    } else {
        Modifier.clip(RoundedCornerShape(18.dp))
    }

    Row(
        modifier = Modifier
            .then(backgroundModifier)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = if (isSelected) Color.White else Color.White.copy(alpha = 0.65f),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = title,
            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.65f),
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

/**
 * Polished Glassmorphism Coming Soon Modal Dialog
 */
@Composable
fun ComingSoonDialog(
    featureTitle: String,
    featureDescription: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(26.dp),
            color = Color(0xD9101426),
            border = BorderStroke(
                1.dp,
                Brush.linearGradient(
                    listOf(
                        Color.White.copy(alpha = 0.50f),
                        AnimeGlowPink.copy(alpha = 0.40f),
                        Color.White.copy(alpha = 0.15f)
                    )
                )
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .shadow(24.dp, RoundedCornerShape(26.dp), spotColor = AnimeGlowPink.copy(0.4f))
                .testTag("coming_soon_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(AnimeGlowPink, AnimeGlowCyan)
                            )
                        )
                        .border(2.dp, Color.White.copy(alpha = 0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Stars,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = featureTitle,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                ComingSoonBadge()

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = featureDescription,
                    color = Color.White.copy(alpha = 0.82f),
                    fontSize = 13.5.sp,
                    lineHeight = 19.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(22.dp))

                // Glass confirmation button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(AnimeGlowPink, AnimeGlowCyan)
                            )
                        )
                        .clickable(onClick = onDismiss)
                        .padding(vertical = 12.dp)
                        .testTag("coming_soon_dialog_dismiss"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Got it",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * Hamburger Glassmorphism Menu Dialog - Streamlined strictly to: Profile, Progress, and About STUDYPAL
 */
@Composable
fun GlassDrawerMenu(
    isOpen: Boolean,
    onClose: () -> Unit,
    onProfileClick: () -> Unit = {},
    onProgressClick: () -> Unit = {},
    onAboutClick: () -> Unit = {}
) {
    if (!isOpen) return

    Dialog(onDismissRequest = onClose) {
        Surface(
            shape = RoundedCornerShape(26.dp),
            color = Color(0xE60E1224),
            border = BorderStroke(
                1.dp,
                Brush.verticalGradient(
                    listOf(Color.White.copy(0.45f), Color.White.copy(0.15f))
                )
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .shadow(24.dp, RoundedCornerShape(26.dp), spotColor = Color(0x70000000))
                .testTag("glass_drawer_menu")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header with close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_app_logo),
                            contentDescription = "STUDYPAL Logo",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .border(1.dp, Color.White.copy(alpha = 0.35f), RoundedCornerShape(10.dp)),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "STUDYPAL",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Smart Study Suite",
                                color = Color.White.copy(alpha = 0.65f),
                                fontSize = 11.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.12f))
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Close Menu",
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Item 1: Profile
                DrawerMenuItem(
                    title = stringResource(R.string.menu_profile),
                    subtitle = "User profile, active grade & achievements",
                    icon = Icons.Rounded.Person,
                    testTag = "menu_item_profile",
                    showComingSoonBadge = false,
                    onClick = {
                        onClose()
                        onProfileClick()
                    }
                )

                // Item 2: Progress (Direct shortcut to local dual-view Progress Tracker)
                DrawerMenuItem(
                    title = stringResource(R.string.nav_progress),
                    subtitle = "Pie chart & syllabus mastery breakdown",
                    icon = Icons.Rounded.Assessment,
                    testTag = "menu_item_progress",
                    showComingSoonBadge = false,
                    onClick = {
                        onClose()
                        onProgressClick()
                    }
                )

                // Item 3: About STUDYPAL
                DrawerMenuItem(
                    title = stringResource(R.string.menu_about),
                    subtitle = "Mission, study rules & system architecture",
                    icon = Icons.Rounded.Info,
                    testTag = "menu_item_about",
                    showComingSoonBadge = false,
                    onClick = {
                        onClose()
                        onAboutClick()
                    }
                )
            }
        }
    }
}

/**
 * Informational dialog detailing STUDYPAL's mission, core rules, and build architecture.
 */
@Composable
fun AboutStudyPalDialog(
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(26.dp),
            color = Color(0xF20F1322),
            border = BorderStroke(
                1.dp,
                Brush.verticalGradient(
                    listOf(
                        Color.White.copy(alpha = 0.55f),
                        Color(0xFF8E8E98).copy(alpha = 0.35f),
                        Color.White.copy(alpha = 0.15f)
                    )
                )
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
                .shadow(28.dp, RoundedCornerShape(26.dp), spotColor = Color(0x80000000))
                .testTag("about_studypal_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header with App Logo
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_app_logo),
                            contentDescription = "STUDYPAL Logo",
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.5.dp, Color.White.copy(alpha = 0.45f), RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "STUDYPAL",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.6.sp
                            )
                            Text(
                                text = "Smart Study Suite • CBSE Standards",
                                color = Color.White.copy(alpha = 0.70f),
                                fontSize = 11.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.12f))
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Close",
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Mission Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White.copy(alpha = 0.06f))
                        .border(1.dp, Color.White.copy(alpha = 0.14f), RoundedCornerShape(14.dp))
                        .padding(14.dp)
                ) {
                    Column {
                        Text(
                            text = "APP MISSION",
                            color = AnimeGlowGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "STUDYPAL is an account-free, zero-subscription academic suite empowering students to master their syllabus. Combining anime-inspired focus aesthetics with authentic CBSE curriculums (Grades 9–12), formula sheets, and mock exams running 100% locally offline.",
                            color = Color.White.copy(alpha = 0.88f),
                            fontSize = 12.5.sp,
                            lineHeight = 17.5.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Core Academic & Focus Rules Section
                Text(
                    text = "CORE STUDY RULES & PROTOCOLS",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.6.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Rule 1: 25-Min Mandatory Timer
                RuleDetailCard(
                    title = "25-Min Mandatory Study Timer",
                    description = "Enforces deep cognitive focus. A chapter study session cannot be exited early and must run its full 25-minute duration before the 80-mark chapter test unlocks.",
                    badge = "Focus Rule",
                    accentColor = AnimeGlowCyan
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Rule 2: Class-Wide Lock Protocol
                RuleDetailCard(
                    title = "Class-Wide Lock Protocol",
                    description = "Starting an incomplete chapter locks all other chapters in that grade. Other units remain locked until the candidate demonstrates mastery by scoring 60+ marks on the chapter test.",
                    badge = "Lock Protocol",
                    accentColor = AnimeGlowPink
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Rule 3: CBSE-Standard Evaluation
                RuleDetailCard(
                    title = "CBSE 80-Mark Evaluation",
                    description = "Assessments strictly require 60+ marks out of 80 (75%+) to pass. Only passed tests are recorded on the local Progress Tracker; failed tests (< 60) are never counted as progress.",
                    badge = "75%+ Benchmark",
                    accentColor = Color(0xFF06D6A0)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Rule 4: Foreground App Blocker
                RuleDetailCard(
                    title = "Distraction App Blocker",
                    description = "Suppresses distracting apps during active study intervals. Attempting to switch apps triggers an immediate focus alert returning you to your study session.",
                    badge = "Integrity",
                    accentColor = AnimeGlowGold
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Build & Version Information
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.05f))
                        .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "STUDYPAL Build",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Version 1.0.0 • Pure Offline Local Architecture",
                                color = Color.White.copy(alpha = 0.6f),
                                fontSize = 11.sp
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF06D6A0).copy(alpha = 0.2f))
                                .border(1.dp, Color(0xFF06D6A0).copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "Verified",
                                color = Color(0xFF06D6A0),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Got It Dismiss Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(AnimeGlowPink, AnimeGlowCyan)
                            )
                        )
                        .clickable(onClick = onDismiss)
                        .padding(vertical = 12.dp)
                        .testTag("about_dialog_dismiss"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Understood",
                        color = Color.White,
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun RuleDetailCard(
    title: String,
    description: String,
    badge: String,
    accentColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.05f))
            .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(accentColor.copy(alpha = 0.2f))
                        .border(1.dp, accentColor.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = badge,
                        color = accentColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                color = Color.White.copy(alpha = 0.78f),
                fontSize = 11.5.sp,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
private fun DrawerMenuItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    testTag: String,
    showComingSoonBadge: Boolean = true,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.08f))
            .border(1.dp, Color.White.copy(alpha = 0.14f), RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.14f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AnimeGlowCyan,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = subtitle,
                color = Color.White.copy(alpha = 0.60f),
                fontSize = 11.sp
            )
        }
        if (showComingSoonBadge) {
            ComingSoonBadge()
        } else {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White.copy(alpha = 0.12f))
                    .padding(horizontal = 9.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Open",
                    color = AnimeGlowCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
