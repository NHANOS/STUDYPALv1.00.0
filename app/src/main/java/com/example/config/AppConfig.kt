package com.example.config

/**
 * Model representing an external link or upcoming portal in the STUDYPAL app.
 *
 * @param title Clean, human-readable name (e.g. "PVR Play", "Flixeo", "HiAnime", "Specter Scans").
 * @param url The destination web URL.
 * @param isUpcoming Flag indicating if this is an upcoming portal card. When true, tapping displays an in-app upcoming dialog.
 * @param subtitle Optional short description or domain label.
 */
data class ExternalLinkItem(
    val title: String,
    val url: String = "",
    val isUpcoming: Boolean = false,
    val subtitle: String? = null
)

/**
 * Single centralized configuration registry for all external links across STUDYPAL.
 *
 * Contains:
 * - Study & STUDYPAL flagship links
 * - Movies streaming portals & upcoming card
 * - Anime streaming portals & upcoming card
 * - Manga reading portals & upcoming card
 *
 * Easily edit, add, or reorder links here; all UI grids and WebViews update dynamically.
 */
object AppConfig {

    // ==========================================
    // 1. Study Suite Flagship Links
    // ==========================================
    val studyLink = ExternalLinkItem(
        title = "Study",
        url = "https://studypanda.live/nexttoppers",
        subtitle = "studypanda.live"
    )

    val studyXLink = ExternalLinkItem(
        title = "STUDYPAL",
        url = "https://pw.muftukmall.in/",
        subtitle = "pw.muftukmall.in"
    )

    val notesLink = ExternalLinkItem(
        title = "Study Notes",
        url = "https://aglasem.com/",
        subtitle = "aglasem.com"
    )

    val questionSolvingLink = ExternalLinkItem(
        title = "Question Solving",
        url = "https://math.bot/",
        subtitle = "math.bot"
    )

    val studyUrl: String get() = studyLink.url
    val studyXUrl: String get() = studyXLink.url
    val notesUrl: String get() = notesLink.url
    val questionSolvingUrl: String get() = questionSolvingLink.url

    // ==========================================
    // 2. Movies (7 Active Streaming Portals + 1 Upcoming Card)
    // ==========================================
    val movieLinks: List<ExternalLinkItem> = listOf(
        ExternalLinkItem(
            title = "PVR Play",
            url = "https://ghar-baithe-free-movies-dekho-bina-rukawat-pvrplay-hd.pvrplay.fun/",
            subtitle = "pvrplay.fun"
        ),
        ExternalLinkItem(
            title = "Flixeo",
            url = "https://flixeo.tv/home",
            subtitle = "flixeo.tv"
        ),
        ExternalLinkItem(
            title = "PixelFlix",
            url = "https://pixelflix.cc/",
            subtitle = "pixelflix.cc"
        ),
        ExternalLinkItem(
            title = "MeowTV",
            url = "https://meowtv.ru/",
            subtitle = "meowtv.ru"
        ),
        ExternalLinkItem(
            title = "NetOut",
            url = "https://netout.pages.dev/home",
            subtitle = "netout.pages.dev"
        ),
        ExternalLinkItem(
            title = "RedFlix",
            url = "https://redflix.co/",
            subtitle = "redflix.co"
        ),
        ExternalLinkItem(
            title = "SkyFlixer",
            url = "https://skyflixer.fun/browse",
            subtitle = "skyflixer.fun"
        ),
        ExternalLinkItem(
            title = "Upcoming Movies",
            url = "",
            isUpcoming = true,
            subtitle = "Coming Soon"
        )
    )

    // ==========================================
    // 3. Anime (13 Active Streaming Portals + 1 Upcoming Card)
    // ==========================================
    val animeLinks: List<ExternalLinkItem> = listOf(
        ExternalLinkItem(
            title = "Toon Stream",
            url = "https://toon-stream.site/home",
            subtitle = "toon-stream.site"
        ),
        ExternalLinkItem(
            title = "AnimixStream",
            url = "https://animixstream.com/",
            subtitle = "animixstream.com"
        ),
        ExternalLinkItem(
            title = "Anime Salt",
            url = "https://animesalt.cx/",
            subtitle = "animesalt.cx"
        ),
        ExternalLinkItem(
            title = "AniFlix",
            url = "https://aniflix.uno/",
            subtitle = "aniflix.uno"
        ),
        ExternalLinkItem(
            title = "Anime Hindi Dubbed",
            url = "https://animehindidubbed.in/",
            subtitle = "animehindidubbed.in"
        ),
        ExternalLinkItem(
            title = "Watch Anime World",
            url = "https://watchanimeworld.one/",
            subtitle = "watchanimeworld.one"
        ),
        ExternalLinkItem(
            title = "Hindi Anime Verse",
            url = "https://hindianimeverse.org/",
            subtitle = "hindianimeverse.org"
        ),
        ExternalLinkItem(
            title = "Anime TV Plus",
            url = "https://animetvplus.xyz/",
            subtitle = "animetvplus.xyz"
        ),
        ExternalLinkItem(
            title = "AnimeFlix",
            url = "https://animeflix.app/",
            subtitle = "animeflix.app"
        ),
        ExternalLinkItem(
            title = "HiAnime",
            url = "https://hianime.ro/home",
            subtitle = "hianime.ro"
        ),
        ExternalLinkItem(
            title = "Yenime",
            url = "https://yenime.net/",
            subtitle = "yenime.net"
        ),
        ExternalLinkItem(
            title = "AniStream",
            url = "https://anistream.one/home",
            subtitle = "anistream.one"
        ),
        ExternalLinkItem(
            title = "ReAnime",
            url = "https://reanime.to/home",
            subtitle = "reanime.to"
        ),
        ExternalLinkItem(
            title = "Upcoming Anime",
            url = "",
            isUpcoming = true,
            subtitle = "Coming Soon"
        )
    )

    // ==========================================
    // 4. Manga (5 Active Manga Portals + 1 Upcoming Card)
    // ==========================================
    val mangaLinks: List<ExternalLinkItem> = listOf(
        ExternalLinkItem(
            title = "Specter Scans",
            url = "https://specterscans.com/",
            subtitle = "specterscans.com"
        ),
        ExternalLinkItem(
            title = "Manga Ball",
            url = "https://mangaball.com/",
            subtitle = "mangaball.com"
        ),
        ExternalLinkItem(
            title = "Kagane",
            url = "https://kagane.net/",
            subtitle = "kagane.net"
        ),
        ExternalLinkItem(
            title = "Manga Dot",
            url = "https://mangadot.net/",
            subtitle = "mangadot.net"
        ),
        ExternalLinkItem(
            title = "Manga Buddy",
            url = "https://mangabuddy1.co.uk/home",
            subtitle = "mangabuddy1.co.uk"
        ),
        ExternalLinkItem(
            title = "Upcoming Manga",
            url = "",
            isUpcoming = true,
            subtitle = "Coming Soon"
        )
    )
}
