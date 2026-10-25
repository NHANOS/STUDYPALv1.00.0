package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AppBlocking
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.data.ProfileManager
import com.example.data.StudyLockManager
import com.example.model.StudyRepository
import com.example.ui.background.AnimeBackgroundContainer
import com.example.ui.background.SessionBackgroundManager
import com.example.ui.components.AboutStudyPalDialog
import com.example.ui.components.AnimeGlowCyan
import com.example.ui.components.AnimeGlowPink
import com.example.ui.components.ComingSoonDialog
import com.example.ui.components.GlassBottomBar
import com.example.ui.components.GlassDrawerMenu
import com.example.ui.components.GlassTopBar
import com.example.ui.screens.ChapterStudyScreen
import com.example.ui.screens.ChapterTestScreen
import com.example.ui.screens.ChatAiScreen
import com.example.ui.screens.ClassSubjectsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.InAppWebViewScreen
import com.example.ui.screens.MovieZoneScreen
import com.example.config.AppConfig
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SubjectChaptersScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.model.SundayTestPaper
import com.example.model.SundayUserResponse
import com.example.model.SundayTestResult
import com.example.data.SundayTestManager
import com.example.service.SundayTestEvaluator
import com.example.ui.screens.sundaytest.SundayGateScreen
import com.example.ui.screens.sundaytest.SundayExamScreen
import com.example.ui.screens.sundaytest.SundayEvaluationLoadingScreen
import com.example.ui.screens.sundaytest.SundayResultScreen
import com.example.ui.screens.ProgressTrackerScreen
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf

sealed class Screen {
    data object Home : Screen()
    data object Profile : Screen()
    data object MovieZone : Screen()
    data object ChatAi : Screen()
    data class InAppBrowser(val title: String, val url: String) : Screen()
    data class ClassSubjects(val classId: String) : Screen()
    data class SubjectChapters(val classId: String, val subjectId: String) : Screen()
    data class ChapterStudy(val classId: String, val subjectId: String, val chapterId: String) : Screen()
    data class ChapterTest(val classId: String, val subjectId: String, val chapterId: String) : Screen()
    data object SundayTestGate : Screen()
    data class SundayTestExam(val paper: SundayTestPaper) : Screen()
    data class SundayTestEvaluating(
        val paper: SundayTestPaper,
        val responses: Map<String, SundayUserResponse>,
        val timeTakenSeconds: Int
    ) : Screen()
    data class SundayTestResultDisplay(val result: SundayTestResult) : Screen()
    data object ProgressTracker : Screen()
}

data class ComingSoonState(
    val title: String,
    val description: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        handleBlockedAppIntent(intent)
        setContent {
            MyApplicationTheme {
                StudyXApp()
            }
        }
    }

    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleBlockedAppIntent(intent)
    }

    private fun handleBlockedAppIntent(intent: android.content.Intent?) {
        val blockedApp = intent?.getStringExtra(com.example.service.StudyBlockerService.EXTRA_BLOCKED_APP_KEY)
        if (!blockedApp.isNullOrBlank()) {
            com.example.data.StudyLockManager.getInstance(this).notifyBlockedAppTriggered(blockedApp)
        }
    }
}

@Composable
fun StudyXApp() {
    val context = LocalContext.current

    // Unified single source of truth for user profile
    val profileManager = remember { ProfileManager.getInstance(context) }
    val userProfile by profileManager.profileState.collectAsStateWithLifecycle()

    // Session-consistent fixed random background: selected once per application launch
    val sessionTheme = remember { SessionBackgroundManager.getSessionTheme() }

    // Navigation backstack state
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }

    // Dialog & Drawer state
    var isDrawerOpen by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var comingSoonInfo by remember { mutableStateOf<ComingSoonState?>(null) }

    // Bottom Navigation Bar selection
    val bottomNavTab = when (currentScreen) {
        Screen.Home -> 0
        Screen.SundayTestGate, is Screen.SundayTestExam, is Screen.SundayTestEvaluating, is Screen.SundayTestResultDisplay -> 1
        Screen.ProgressTracker -> 2
        else -> 0
    }

    // Android System Back Button Handler
    BackHandler(enabled = currentScreen !is Screen.Home) {
        currentScreen = when (val s = currentScreen) {
            is Screen.ChatAi -> Screen.Home
            is Screen.Profile -> Screen.Home
            is Screen.MovieZone -> Screen.Home
            is Screen.InAppBrowser -> Screen.Home
            is Screen.ProgressTracker -> Screen.Home
            is Screen.SundayTestGate -> Screen.Home
            is Screen.SundayTestExam -> Screen.SundayTestGate
            is Screen.SundayTestEvaluating -> Screen.SundayTestGate
            is Screen.SundayTestResultDisplay -> Screen.SundayTestGate
            is Screen.ChapterTest -> Screen.ChapterStudy(s.classId, s.subjectId, s.chapterId)
            is Screen.ChapterStudy -> Screen.SubjectChapters(s.classId, s.subjectId)
            is Screen.SubjectChapters -> Screen.ClassSubjects(s.classId)
            is Screen.ClassSubjects -> Screen.Home
            Screen.Home -> Screen.Home
        }
    }

    AnimeBackgroundContainer(theme = sessionTheme) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Scrollable Content Area placed between top bar and bottom bar
            val topBarPadding = 80.dp
            val bottomBarPadding = 86.dp

            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    (fadeIn() + slideInHorizontally { width -> width / 3 }) togetherWith
                            (fadeOut() + slideOutHorizontally { width -> -width / 3 })
                },
                label = "ScreenTransition",
                modifier = Modifier.fillMaxSize()
            ) { targetScreen ->
                val contentPadding = PaddingValues(top = topBarPadding, bottom = bottomBarPadding)

                when (targetScreen) {
                    is Screen.Home -> {
                        HomeScreen(
                            currentTheme = sessionTheme,
                            onClassSelected = { classId ->
                                currentScreen = Screen.ClassSubjects(classId)
                            },
                            onMovieZoneClick = {
                                currentScreen = Screen.MovieZone
                            },
                            onStudyClick = {
                                currentScreen = Screen.InAppBrowser(
                                    title = AppConfig.studyLink.title,
                                    url = AppConfig.studyUrl
                                )
                            },
                            onStudyXClick = {
                                currentScreen = Screen.InAppBrowser(
                                    title = AppConfig.studyXLink.title,
                                    url = AppConfig.studyXUrl
                                )
                            },
                            onNotesClick = {
                                currentScreen = Screen.InAppBrowser(
                                    title = AppConfig.notesLink.title,
                                    url = AppConfig.notesUrl
                                )
                            },
                            onQuestionSolvingClick = {
                                currentScreen = Screen.InAppBrowser(
                                    title = AppConfig.questionSolvingLink.title,
                                    url = AppConfig.questionSolvingUrl
                                )
                            },
                            onChatAiClick = {
                                currentScreen = Screen.ChatAi
                            },
                            onFeatureClick = { title, desc ->
                                comingSoonInfo = ComingSoonState(title, desc)
                            },
                            contentPadding = contentPadding
                        )
                    }

                    is Screen.Profile -> {
                        ProfileScreen(
                            onBackClick = { currentScreen = Screen.Home },
                            contentPadding = contentPadding
                        )
                    }

                    is Screen.MovieZone -> {
                        MovieZoneScreen(
                            onBackClick = { currentScreen = Screen.Home },
                            onOpenUrl = { title, url ->
                                currentScreen = Screen.InAppBrowser(title = title, url = url)
                            },
                            onUpcomingClick = { title, categoryTitle ->
                                comingSoonInfo = ComingSoonState(
                                    title = title,
                                    description = "New streaming portals and direct sources for $categoryTitle are being verified and will be available in the next release."
                                )
                            },
                            contentPadding = contentPadding
                        )
                    }

                    is Screen.ClassSubjects -> {
                        val classInfo = StudyRepository.getClass(targetScreen.classId)
                        if (classInfo != null) {
                            ClassSubjectsScreen(
                                classInfo = classInfo,
                                onBackClick = { currentScreen = Screen.Home },
                                onSubjectClick = { subjectId ->
                                    currentScreen = Screen.SubjectChapters(targetScreen.classId, subjectId)
                                },
                                contentPadding = contentPadding
                            )
                        } else {
                            currentScreen = Screen.Home
                        }
                    }

                    is Screen.SubjectChapters -> {
                        val classInfo = StudyRepository.getClass(targetScreen.classId)
                        val subjectInfo = StudyRepository.getSubject(targetScreen.classId, targetScreen.subjectId)
                        if (classInfo != null && subjectInfo != null) {
                            SubjectChaptersScreen(
                                classInfo = classInfo,
                                subjectInfo = subjectInfo,
                                onBackClick = {
                                    currentScreen = Screen.ClassSubjects(targetScreen.classId)
                                },
                                onChapterClick = { chapterId ->
                                    currentScreen = Screen.ChapterStudy(
                                        targetScreen.classId,
                                        targetScreen.subjectId,
                                        chapterId
                                    )
                                },
                                contentPadding = contentPadding
                            )
                        } else {
                            currentScreen = Screen.Home
                        }
                    }

                    is Screen.ChapterStudy -> {
                        val classInfo = StudyRepository.getClass(targetScreen.classId)
                        val subjectInfo = StudyRepository.getSubject(targetScreen.classId, targetScreen.subjectId)
                        val chapterInfo = StudyRepository.getChapter(
                            targetScreen.classId,
                            targetScreen.subjectId,
                            targetScreen.chapterId
                        )
                        if (classInfo != null && subjectInfo != null && chapterInfo != null) {
                            ChapterStudyScreen(
                                classInfo = classInfo,
                                subjectInfo = subjectInfo,
                                chapterInfo = chapterInfo,
                                onBackClick = {
                                    currentScreen = Screen.SubjectChapters(
                                        targetScreen.classId,
                                        targetScreen.subjectId
                                    )
                                },
                                onStartTestClick = {
                                    currentScreen = Screen.ChapterTest(
                                        targetScreen.classId,
                                        targetScreen.subjectId,
                                        targetScreen.chapterId
                                    )
                                },
                                contentPadding = contentPadding
                            )
                        } else {
                            currentScreen = Screen.Home
                        }
                    }

                    is Screen.ChapterTest -> {
                        val classInfo = StudyRepository.getClass(targetScreen.classId)
                        val subjectInfo = StudyRepository.getSubject(targetScreen.classId, targetScreen.subjectId)
                        val chapterInfo = StudyRepository.getChapter(
                            targetScreen.classId,
                            targetScreen.subjectId,
                            targetScreen.chapterId
                        )
                        if (classInfo != null && subjectInfo != null && chapterInfo != null) {
                            ChapterTestScreen(
                                classInfo = classInfo,
                                subjectInfo = subjectInfo,
                                chapterInfo = chapterInfo,
                                onBackClick = {
                                    currentScreen = Screen.ChapterStudy(
                                        targetScreen.classId,
                                        targetScreen.subjectId,
                                        targetScreen.chapterId
                                    )
                                },
                                onTestPassedAndUnlocked = {
                                    currentScreen = Screen.SubjectChapters(
                                        targetScreen.classId,
                                        targetScreen.subjectId
                                    )
                                },
                                contentPadding = contentPadding
                            )
                        } else {
                            currentScreen = Screen.Home
                        }
                    }

                    is Screen.SundayTestGate -> {
                        SundayGateScreen(
                            onBackClick = { currentScreen = Screen.Home },
                            onStartExam = { paper ->
                                currentScreen = Screen.SundayTestExam(paper)
                            },
                            contentPadding = contentPadding
                        )
                    }

                    is Screen.SundayTestExam -> {
                        SundayExamScreen(
                            paper = targetScreen.paper,
                            onBackExit = { currentScreen = Screen.SundayTestGate },
                            onSubmitExam = { responses, timeTaken ->
                                currentScreen = Screen.SundayTestEvaluating(
                                    paper = targetScreen.paper,
                                    responses = responses,
                                    timeTakenSeconds = timeTaken
                                )
                            },
                            contentPadding = contentPadding
                        )
                    }

                    is Screen.SundayTestEvaluating -> {
                        var currentLayer by remember { mutableIntStateOf(1) }
                        var statusMessage by remember { mutableStateOf("Starting Two-Layer Evaluation...") }

                        LaunchedEffect(targetScreen.paper.id) {
                            val evaluatedResult = SundayTestEvaluator.evaluatePaper(
                                paper = targetScreen.paper,
                                userResponses = targetScreen.responses,
                                timeTakenSeconds = targetScreen.timeTakenSeconds,
                                onProgressUpdate = { layer, msg ->
                                    currentLayer = layer
                                    statusMessage = msg
                                }
                            )

                            // Persist verified result and attempt
                            val sundayManager = SundayTestManager.getInstance(context)
                            sundayManager.recordAttempt(
                                score = evaluatedResult.score,
                                isPassed = evaluatedResult.isPassed,
                                subjectName = evaluatedResult.subjectName
                            )
                            sundayManager.saveTestResult(evaluatedResult)

                            if (evaluatedResult.isPassed) {
                                // Update user progress metrics
                                profileManager.recordStudyProgress(3.0f, chapterPassed = true)
                            }

                            currentScreen = Screen.SundayTestResultDisplay(evaluatedResult)
                        }

                        SundayEvaluationLoadingScreen(
                            currentLayer = currentLayer,
                            statusMessage = statusMessage
                        )
                    }

                    is Screen.SundayTestResultDisplay -> {
                        SundayResultScreen(
                            result = targetScreen.result,
                            onBackToPortal = { currentScreen = Screen.SundayTestGate },
                            onBackToHome = { currentScreen = Screen.Home },
                            contentPadding = contentPadding
                        )
                    }

                    is Screen.InAppBrowser -> {
                        InAppWebViewScreen(
                            title = targetScreen.title,
                            url = targetScreen.url,
                            onExit = { currentScreen = Screen.Home }
                        )
                    }

                    is Screen.ChatAi -> {
                        ChatAiScreen(
                            onBackClick = { currentScreen = Screen.Home }
                        )
                    }

                    is Screen.ProgressTracker -> {
                        ProgressTrackerScreen(
                            onNavigateToClass = { classId ->
                                currentScreen = Screen.ClassSubjects(classId)
                            },
                            contentPadding = contentPadding
                        )
                    }
                }
            }

            // Fixed Top & Bottom Navigation Bars (hidden during InAppBrowser, ChatAi, ChapterTest, and SundayTestExam)
            if (currentScreen !is Screen.InAppBrowser && currentScreen !is Screen.ChatAi && currentScreen !is Screen.ChapterTest && currentScreen !is Screen.SundayTestExam && currentScreen !is Screen.SundayTestEvaluating) {
                GlassTopBar(
                    title = "STUDYPAL",
                    userProfile = userProfile,
                    onMenuClick = { isDrawerOpen = true },
                    onProfileClick = {
                        currentScreen = Screen.Profile
                    },
                    modifier = Modifier.align(Alignment.TopCenter)
                )

                GlassBottomBar(
                    selectedTab = bottomNavTab,
                    onTabSelected = { tabIndex ->
                        when (tabIndex) {
                            0 -> currentScreen = Screen.Home
                            1 -> currentScreen = Screen.SundayTestGate
                            2 -> currentScreen = Screen.ProgressTracker
                        }
                    },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }

            // Glass Drawer Menu (Hamburger popup: Profile, Progress, About STUDYPAL)
            GlassDrawerMenu(
                isOpen = isDrawerOpen,
                onClose = { isDrawerOpen = false },
                onProfileClick = {
                    currentScreen = Screen.Profile
                },
                onProgressClick = {
                    currentScreen = Screen.ProgressTracker
                },
                onAboutClick = {
                    showAboutDialog = true
                }
            )

            // About STUDYPAL Information Modal
            if (showAboutDialog) {
                AboutStudyPalDialog(
                    onDismiss = { showAboutDialog = false }
                )
            }

            // Polished Coming Soon Dialog for unreleased modules and Movie Zone cards
            comingSoonInfo?.let { info ->
                ComingSoonDialog(
                    featureTitle = info.title,
                    featureDescription = info.description,
                    onDismiss = { comingSoonInfo = null }
                )
            }

            // Distraction App Blocker Alert Dialog
            val studyLockManager = remember { StudyLockManager.getInstance(context) }
            val blockedAppAlert by studyLockManager.lastBlockedAppAlert.collectAsStateWithLifecycle()
            blockedAppAlert?.let { appName ->
                AlertDialog(
                    onDismissRequest = { studyLockManager.clearBlockedAppAlert() },
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.AppBlocking,
                                contentDescription = null,
                                tint = AnimeGlowPink,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Study Focus Enforced!", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    },
                    text = {
                        Text(
                            text = "'$appName' is blocked! This app is blocked until your study session ends.",
                            color = Color.White.copy(alpha = 0.95f),
                            lineHeight = 20.sp
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = { studyLockManager.clearBlockedAppAlert() }) {
                            Text("Return to Study", color = AnimeGlowCyan, fontWeight = FontWeight.Bold)
                        }
                    },
                    containerColor = Color(0xFF1E243A)
                )
            }
        }
    }
}
