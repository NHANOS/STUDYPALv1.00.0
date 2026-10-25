package com.example.ui.background

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.R

/**
 * The five dedicated, low-brightness, soft-lit original anime background themes.
 * Each corresponds to a local XML vector asset in res/drawable/.
 */
enum class AnimeBackgroundTheme(
    val displayName: String,
    val moodDescription: String,
    @DrawableRes val drawableResId: Int,
    val assetFileName: String
) {
    RAINY_EVENING(
        displayName = "Rainy Anime Evening",
        moodDescription = "Calm evening street with cozy warm lantern reflections",
        drawableResId = R.drawable.bg_rainy_anime_evening,
        assetFileName = "res/drawable/bg_rainy_anime_evening.xml"
    ),
    PEACEFUL_FLOWER_GARDEN(
        displayName = "Peaceful Flower Garden",
        moodDescription = "Soft pastel blossoms, sage hills and gentle light",
        drawableResId = R.drawable.bg_peaceful_flower_garden,
        assetFileName = "res/drawable/bg_peaceful_flower_garden.xml"
    ),
    OCEAN_BLUE_SKY(
        displayName = "Ocean and Blue Sky",
        moodDescription = "Quiet steel blue sea, gentle waves and misty horizon",
        drawableResId = R.drawable.bg_ocean_blue_sky,
        assetFileName = "res/drawable/bg_ocean_blue_sky.xml"
    ),
    CHERRY_BLOSSOM_EVENING(
        displayName = "Cherry Blossom Evening",
        moodDescription = "Soft dusky rose twilight and serene sakura silhouettes",
        drawableResId = R.drawable.bg_cherry_blossom_evening,
        assetFileName = "res/drawable/bg_cherry_blossom_evening.xml"
    ),
    CALM_STARRY_NIGHT(
        displayName = "Calm Starry Night",
        moodDescription = "Deep quiet midnight sky, crescent moon and still lake",
        drawableResId = R.drawable.bg_calm_starry_night,
        assetFileName = "res/drawable/bg_calm_starry_night.xml"
    )
}

/**
 * Session-scoped background manager.
 * Randomly selects one of the 5 themes on a fresh app launch and keeps it
 * fixed throughout the entire user session.
 */
object SessionBackgroundManager {
    private var sessionTheme: AnimeBackgroundTheme? = null

    fun getSessionTheme(): AnimeBackgroundTheme {
        if (sessionTheme == null) {
            sessionTheme = AnimeBackgroundTheme.entries.random()
        }
        return sessionTheme!!
    }

    /**
     * For manual testing or debugging.
     */
    fun setSessionTheme(theme: AnimeBackgroundTheme) {
        sessionTheme = theme
    }

    fun resetSession() {
        sessionTheme = null
    }
}

/**
 * Background container rendering the session's fixed local background asset
 * behind the entire application with an eye-comfortable translucent glass scrim.
 */
@Composable
fun AnimeBackgroundContainer(
    theme: AnimeBackgroundTheme = remember { SessionBackgroundManager.getSessionTheme() },
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        // 1. Fixed local anime background image
        Image(
            painter = painterResource(id = theme.drawableResId),
            contentDescription = theme.displayName,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 2. Eye-comfortable, low-brightness frosted glass scrim to guarantee readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0x400A0E1A),
                            Color(0x25000000),
                            Color(0x480A0E1A)
                        )
                    )
                )
        )

        // 3. Application UI content
        content()
    }
}
