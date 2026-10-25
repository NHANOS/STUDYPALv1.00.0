package com.example

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.test.core.app.ApplicationProvider
import com.example.chat.data.ImageQuotaManager
import com.example.chat.data.OpenAiService
import com.example.chat.model.ChatMessage
import com.example.chat.model.MessageRole
import com.example.config.AppConfig
import com.example.data.ProfileManager
import com.example.util.AdBlocker
import com.example.model.StudyRepository
import com.example.ui.background.AnimeBackgroundTheme
import com.example.ui.background.SessionBackgroundManager
import com.example.ui.screens.MovieZoneCategory
import com.example.ui.screens.MovieZoneRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("STUDYPAL", appName)
  }

  @Test
  fun `verify classes repository contains four grades`() {
    val classes = StudyRepository.classes
    assertEquals(4, classes.size)
    assertEquals("Class 9", classes[0].name)
    assertEquals("Class 10", classes[1].name)
    assertEquals("Class 11", classes[2].name)
    assertEquals("Class 12", classes[3].name)
  }

  @Test
  fun `verify five background themes and their local drawable assets`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val themes = AnimeBackgroundTheme.entries
    assertEquals(5, themes.size)

    for (theme in themes) {
      val drawable = ContextCompat.getDrawable(context, theme.drawableResId)
      assertNotNull("Drawable resource for ${theme.displayName} must exist", drawable)
    }
  }

  @Test
  fun `verify session background manager returns consistent theme`() {
    SessionBackgroundManager.resetSession()
    val theme1 = SessionBackgroundManager.getSessionTheme()
    val theme2 = SessionBackgroundManager.getSessionTheme()
    assertEquals("Session theme must remain identical across calls within same session", theme1, theme2)
  }

  @Test
  fun `verify profile manager name update and immediate stateflow synchronization`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val profileManager = ProfileManager.getInstance(context)

    // Test saving a valid name
    val success = profileManager.saveName("Aarav Sharma")
    assertTrue(success)

    val profile = profileManager.getProfile()
    assertEquals("Aarav Sharma", profile.name)
    assertEquals("A", profile.displayInitial)

    // Verify reactive stateFlow emits new state synchronously
    assertEquals("Aarav Sharma", profileManager.profileState.value.name)
    assertEquals("A", profileManager.profileState.value.displayInitial)

    // Test rejecting empty or whitespace-only name
    val blankSuccess = profileManager.saveName("   ")
    assertFalse(blankSuccess)
    // Name should remain unchanged
    assertEquals("Aarav Sharma", profileManager.getProfile().name)
  }

  @Test
  fun `verify movie zone categories and dynamic card counts from app config`() {
    // 1. Movie category: 7 active streaming portals + 1 upcoming card = 8 cards
    val movieCards = MovieZoneRepository.movieCards
    assertEquals(8, movieCards.size)
    assertEquals("PVR Play", movieCards[0].label)
    assertEquals(MovieZoneCategory.MOVIE, movieCards[0].category)
    assertFalse(movieCards[0].isUpcoming)
    assertEquals("https://ghar-baithe-free-movies-dekho-bina-rukawat-pvrplay-hd.pvrplay.fun/", movieCards[0].targetUrl)

    val lastMovie = movieCards.last()
    assertEquals("Upcoming Movies", lastMovie.label)
    assertTrue(lastMovie.isUpcoming)

    // 2. Anime category: 13 active portals + 1 upcoming card = 14 cards
    val animeCards = MovieZoneRepository.animeCards
    assertEquals(14, animeCards.size)
    assertEquals("Toon Stream", animeCards[0].label)
    assertEquals(MovieZoneCategory.ANIME, animeCards[0].category)
    assertFalse(animeCards[0].isUpcoming)
    assertEquals("https://toon-stream.site/home", animeCards[0].targetUrl)

    val lastAnime = animeCards.last()
    assertEquals("Upcoming Anime", lastAnime.label)
    assertTrue(lastAnime.isUpcoming)

    // 3. Manga category: 5 active portals + 1 upcoming card = 6 cards
    val mangaCards = MovieZoneRepository.mangaCards
    assertEquals(6, mangaCards.size)
    assertEquals("Specter Scans", mangaCards[0].label)
    assertEquals(MovieZoneCategory.MANGA, mangaCards[0].category)
    assertFalse(mangaCards[0].isUpcoming)
    assertEquals("https://specterscans.com/", mangaCards[0].targetUrl)

    val lastManga = mangaCards.last()
    assertEquals("Upcoming Manga", lastManga.label)
    assertTrue(lastManga.isUpcoming)
  }

  @Test
  fun `verify cbse curriculum chapters for all classes`() {
    val class9 = StudyRepository.getClass("class_9")
    assertNotNull(class9)
    assertEquals(10, class9!!.subjects.size)
    val c9Math = class9.subjects.find { it.id == "c9_math" }
    assertNotNull(c9Math)
    assertEquals(12, c9Math!!.chapters.size)

    val class10 = StudyRepository.getClass("class_10")
    assertNotNull(class10)
    assertEquals(10, class10!!.subjects.size)
    val c10Math = class10.subjects.find { it.id == "c10_math" }
    assertNotNull(c10Math)
    assertEquals(14, c10Math!!.chapters.size)

    val class11 = StudyRepository.getClass("class_11")
    assertNotNull(class11)
    assertEquals(6, class11!!.subjects.size)
    val c11Bio = class11.subjects.find { it.id == "c11_bio" }
    assertNotNull(c11Bio)
    assertEquals(19, c11Bio!!.chapters.size)

    val class12 = StudyRepository.getClass("class_12")
    assertNotNull(class12)
    assertEquals(6, class12!!.subjects.size)
    val c12Math = class12.subjects.find { it.id == "c12_math" }
    val c12Phys = class12.subjects.find { it.id == "c12_phys" }
    val c12Chem = class12.subjects.find { it.id == "c12_chem" }
    val c12Bio = class12.subjects.find { it.id == "c12_bio" }
    val c12Cs = class12.subjects.find { it.id == "c12_cs" }
    val c12Eco = class12.subjects.find { it.id == "c12_eco" }

    assertNotNull(c12Math)
    assertEquals(13, c12Math!!.chapters.size)
    assertNotNull(c12Phys)
    assertEquals(14, c12Phys!!.chapters.size)
    assertNotNull(c12Chem)
    assertEquals(10, c12Chem!!.chapters.size)
    assertNotNull(c12Bio)
    assertEquals(13, c12Bio!!.chapters.size)
    assertNotNull(c12Cs)
    assertEquals(6, c12Cs!!.chapters.size)
    assertNotNull(c12Eco)
    assertEquals(8, c12Eco!!.chapters.size)
  }

  @Test
  fun `verify internal inapp browser destinations for study study x and notes from app config`() {
    assertEquals("Study", AppConfig.studyLink.title)
    assertEquals("https://studypanda.live/nexttoppers", AppConfig.studyUrl)

    assertEquals("STUDYPAL", AppConfig.studyXLink.title)
    assertEquals("https://pw.muftukmall.in/", AppConfig.studyXUrl)

    // Aglasem Notes configuration
    assertEquals("Study Notes", AppConfig.notesLink.title)
    assertEquals("https://aglasem.com/", AppConfig.notesUrl)

    val notesScreen = Screen.InAppBrowser(AppConfig.notesLink.title, AppConfig.notesUrl)
    assertEquals("Study Notes", notesScreen.title)
    assertEquals("https://aglasem.com/", notesScreen.url)

    // Math.bot Question Solving configuration
    assertEquals("Question Solving", AppConfig.questionSolvingLink.title)
    assertEquals("https://math.bot/", AppConfig.questionSolvingUrl)

    val questionSolvingScreen = Screen.InAppBrowser(AppConfig.questionSolvingLink.title, AppConfig.questionSolvingUrl)
    assertEquals("Question Solving", questionSolvingScreen.title)
    assertEquals("https://math.bot/", questionSolvingScreen.url)
  }

  @Test
  fun `verify client-side ad blocker blocks ad domains and allows safe domains`() {
    // Known ad, tracking, and popup domains must be blocked
    assertTrue(AdBlocker.isAd("https://pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"))
    assertTrue(AdBlocker.isAd("https://googleads.g.doubleclick.net/pagead/ads"))
    assertTrue(AdBlocker.isAd("https://adservice.google.com/adservice.js"))
    assertTrue(AdBlocker.isAd("https://popads.net/serve.js"))
    assertTrue(AdBlocker.isAd("https://adsterra.com/banner.js"))
    assertTrue(AdBlocker.isAd("https://monetag.com/tag.js"))
    assertTrue(AdBlocker.isAd("https://criteo.com/delivery/csm.js"))

    // Safe website domains must NOT be blocked
    assertFalse(AdBlocker.isAd("https://aglasem.com/"))
    assertFalse(AdBlocker.isAd("https://studypanda.live/nexttoppers"))
    assertFalse(AdBlocker.isAd("https://pw.muftukmall.in/"))
    assertFalse(AdBlocker.isAd("https://flixeo.tv/home"))
    assertFalse(AdBlocker.isAd("https://hianime.ro/home"))
    assertFalse(AdBlocker.isAd("https://specterscans.com/"))
    assertFalse(AdBlocker.isAd(null))
    assertFalse(AdBlocker.isAd(""))

    // Verify empty resource response format
    val emptyResponse = AdBlocker.createEmptyResource()
    assertEquals("text/plain", emptyResponse.mimeType)
    assertEquals("utf-8", emptyResponse.encoding)
    assertNotNull(emptyResponse.data)
  }

  @Test
  fun `verify daily image quota limits to exactly two images per day and resets on date change`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val quotaManager = ImageQuotaManager.getInstance(context)

    val day1 = "2026-10-24"
    val day2 = "2026-10-25"

    // Reset to clean slate for day 1
    quotaManager.resetForTesting(day1, 0)
    assertEquals(2, quotaManager.getRemainingCount(day1))
    assertTrue(quotaManager.canGenerateImage(day1))

    // 1st generation
    val firstGenSuccess = quotaManager.recordImageGenerated(day1)
    assertTrue(firstGenSuccess)
    assertEquals(1, quotaManager.getUsedCount(day1))
    assertEquals(1, quotaManager.getRemainingCount(day1))
    assertTrue(quotaManager.canGenerateImage(day1))

    // 2nd generation
    val secondGenSuccess = quotaManager.recordImageGenerated(day1)
    assertTrue(secondGenSuccess)
    assertEquals(2, quotaManager.getUsedCount(day1))
    assertEquals(0, quotaManager.getRemainingCount(day1))
    assertFalse(quotaManager.canGenerateImage(day1))

    // 3rd attempt on same day must be rejected by quota rule
    val thirdGenAttempt = quotaManager.recordImageGenerated(day1)
    assertFalse(thirdGenAttempt)
    assertEquals(2, quotaManager.getUsedCount(day1))
    assertEquals(0, quotaManager.getRemainingCount(day1))
    assertEquals(
      "You have reached your daily limit of 2 generated images. Please try again tomorrow.",
      ImageQuotaManager.QUOTA_EXCEEDED_MESSAGE
    )

    // On new calendar day (day 2), quota must automatically reset to 2 available
    assertEquals(0, quotaManager.getUsedCount(day2))
    assertEquals(2, quotaManager.getRemainingCount(day2))
    assertTrue(quotaManager.canGenerateImage(day2))
  }

  @Test
  fun `verify openai image request detection and prompt extraction`() {
    assertTrue(OpenAiService.isImageRequest("/image an atom model"))
    assertTrue(OpenAiService.isImageRequest("/draw a mitochondrion"))
    assertTrue(OpenAiService.isImageRequest("/img cross section of plant stem"))
    assertTrue(OpenAiService.isImageRequest("Please generate an image of a magnetic field"))
    assertTrue(OpenAiService.isImageRequest("can you create a picture of a benzene ring?"))
    assertFalse(OpenAiService.isImageRequest("Explain Newton's third law of motion"))
    assertFalse(OpenAiService.isImageRequest("Solve quadratic equation 2x^2 + 5x - 3 = 0"))

    // Prompt extraction
    assertEquals("an atom model", OpenAiService.extractImagePrompt("/image an atom model"))
    assertEquals("a mitochondrion", OpenAiService.extractImagePrompt("/draw a mitochondrion"))
    assertEquals("a magnetic field", OpenAiService.extractImagePrompt("generate an image of a magnetic field"))
  }

  @Test
  fun `verify screen chat ai navigation and ephemeral model instantiation`() {
    val chatScreen = Screen.ChatAi
    assertNotNull(chatScreen)

    val msg = ChatMessage(
      role = MessageRole.ASSISTANT,
      text = "STUDYPAL Tutor Ready"
    )
    assertEquals(MessageRole.ASSISTANT, msg.role)
    assertEquals("STUDYPAL Tutor Ready", msg.text)
    assertFalse(msg.isImage)
    assertFalse(msg.isError)
  }

  @Test
  fun `verify openrouter api configuration endpoints headers and model`() {
    // 1. Verify custom OpenRouter endpoints and model
    assertEquals("https://openrouter.ai/api/v1", OpenAiService.BASE_URL)
    assertEquals("https://openrouter.ai/api/v1/chat/completions", OpenAiService.CHAT_COMPLETIONS_URL)
    assertEquals("openrouter/free", OpenAiService.CHAT_MODEL)

    // 2. Verify required OpenRouter identification headers
    assertEquals("https://studypal.app", OpenAiService.HTTP_REFERER)
    assertEquals("STUDYPAL AI Tutor", OpenAiService.X_TITLE)

    // 3. Verify service instance and runtime key management
    val service = OpenAiService()
    service.runtimeApiKeyOverride = "sk-or-v1-testkey12345678"
    assertTrue(service.hasValidApiKey())
    assertEquals("sk-or-v1-testkey12345678", service.effectiveApiKey)
  }

  @Test
  fun `verify chapter test bank generates 80-mark test with 60 passing threshold`() {
    val class10 = StudyRepository.getClass("class_10")
    assertNotNull(class10)
    val physics = class10!!.subjects.find { it.id == "c10_phys" }
    assertNotNull(physics)
    val ch1 = physics!!.chapters.first()

    val questions = com.example.model.ChapterTestBank.generateTestForChapter(class10, physics, ch1)
    assertEquals(20, questions.size)
    assertEquals(80, com.example.model.ChapterTestBank.TOTAL_MARKS)
    assertEquals(60, com.example.model.ChapterTestBank.PASSING_MARKS)

    var totalMarks = 0
    for (q in questions) {
      assertEquals(4, q.marks)
      assertEquals(4, q.options.size)
      assertTrue(q.correctIndex in 0..3)
      totalMarks += q.marks
    }
    assertEquals(80, totalMarks)
  }

  @Test
  fun `verify class-wide chapter locking and unlocking upon passing test with 60+ marks`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val lockManager = com.example.data.StudyLockManager.getInstance(context)

    val classId = "class_10"
    val subjectId = "c10_phys"
    val ch1Id = "c10_phys_ch1"
    val ch2Id = "c10_phys_ch2"
    val mathCh1Id = "c10_math_ch1"

    // 1. Start study session on incomplete chapter
    val session = lockManager.startStudySession(
      classId = classId,
      subjectId = subjectId,
      chapterId = ch1Id,
      chapterTitle = "Light - Reflection and Refraction",
      durationMinutes = 25,
      blockedPackages = setOf("com.google.android.youtube", "com.instagram.android")
    )

    // Minimum duration constraint enforcement
    assertTrue(session.totalDurationMinutes >= 25)
    assertEquals(25 * 60, session.remainingSeconds)
    assertTrue(session.isRunning)
    assertFalse(session.isTimerCompleted)
    assertEquals(2, session.blockedPackages.size)

    // Active chapter itself is accessible
    assertFalse(lockManager.isChapterLocked(classId, ch1Id))

    // Class-wide lock rule: ALL other chapters in same subject and other subjects for this class are locked!
    assertTrue("Other chapter in same subject must be locked", lockManager.isChapterLocked(classId, ch2Id))
    assertTrue("Chapter in different subject for same class must be locked", lockManager.isChapterLocked(classId, mathCh1Id))

    // 2. Score < 60 marks (e.g. 52 marks) fails and keeps other chapters locked
    val failResult = lockManager.recordTestScore(classId, subjectId, ch1Id, score = 52)
    assertFalse(failResult.isPassed)
    assertEquals(52, failResult.score)
    assertEquals(80, failResult.maxScore)
    assertTrue("Chapters must remain locked when score is < 60", lockManager.isChapterLocked(classId, ch2Id))

    // 3. Score >= 60 marks (e.g. 72 marks) passes and unlocks ALL chapters for that class level!
    val passResult = lockManager.recordTestScore(classId, subjectId, ch1Id, score = 72)
    assertTrue(passResult.isPassed)
    assertEquals(72, passResult.score)
    assertTrue(lockManager.isChapterCompleted(ch1Id))

    // Verify all chapters in this class are now unlocked!
    assertFalse("Chapters must be unlocked upon passing with 60+ marks", lockManager.isChapterLocked(classId, ch2Id))
    assertFalse("Other subject chapters must also be unlocked", lockManager.isChapterLocked(classId, mathCh1Id))

    // 4. Starting study session on an ALREADY-COMPLETED chapter must NOT re-trigger the class-wide lock!
    lockManager.startStudySession(
      classId = classId,
      subjectId = subjectId,
      chapterId = ch1Id,
      chapterTitle = "Light - Reflection and Refraction (Revision)",
      durationMinutes = 30,
      blockedPackages = emptySet()
    )
    assertFalse("Re-opening completed chapter must NOT trigger class-wide lock", lockManager.isChapterLocked(classId, ch2Id))
    assertFalse("Other subject chapters must stay unlocked", lockManager.isChapterLocked(classId, mathCh1Id))
  }

  @Test
  fun `verify streamlined drawer menu and bottom navigation labels`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    // Drawer strictly retains: Profile, Progress, About STUDYPAL
    assertEquals("Profile", context.getString(R.string.menu_profile))
    assertEquals("Progress", context.getString(R.string.nav_progress))
    assertEquals("About STUDYPAL", context.getString(R.string.menu_about))

    // Dedicated Sunday Test button label for center bottom bar
    assertEquals("Sunday Test", context.getString(R.string.nav_sunday_test))
  }
}



