package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.OndemandVideo
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.config.AppConfig
import com.example.ui.components.AnimeGlowCyan
import com.example.ui.components.AnimeGlowGold
import com.example.ui.components.AnimeGlowPink
import com.example.ui.components.GlassCard

enum class MovieZoneCategory(
    val title: String,
    val icon: ImageVector,
    val description: String
) {
    MOVIE("Movie", Icons.Rounded.Movie, "Curated educational & inspiring films"),
    ANIME("Anime", Icons.Rounded.OndemandVideo, "Concept animated series & deep-focus episodes"),
    MANGA("Manga", Icons.Rounded.AutoStories, "Illustrated concept stories & graphic knowledge")
}

data class MovieZoneCardItem(
    val id: String,
    val label: String,
    val subtitle: String,
    val category: MovieZoneCategory,
    val gradientColors: List<Color>,
    val targetUrl: String = "",
    val isUpcoming: Boolean = false
)

object MovieZoneRepository {

    private val gradientPalette = listOf(
        listOf(Color(0xFFFF7597), Color(0xFFFF9E7A)),
        listOf(Color(0xFF64D2FF), Color(0xFF5E5CE6)),
        listOf(Color(0xFF06D6A0), Color(0xFF1B9AAA)),
        listOf(Color(0xFFFFD166), Color(0xFFFF7597)),
        listOf(Color(0xFF8A4FFF), Color(0xFFC77DFF)),
        listOf(Color(0xFFFF5964), Color(0xFFEE6C4D)),
        listOf(Color(0xFF3A86FF), Color(0xFF8A4FFF)),
        listOf(Color(0xFF2EC4B6), Color(0xFF06D6A0))
    )

    // Category 1: Movie (dynamically derived from AppConfig.movieLinks)
    val movieCards: List<MovieZoneCardItem>
        get() = AppConfig.movieLinks.mapIndexed { index, item ->
            val gradient = if (item.isUpcoming) {
                listOf(AnimeGlowGold, AnimeGlowPink)
            } else {
                gradientPalette[index % gradientPalette.size]
            }
            MovieZoneCardItem(
                id = "movie_${index + 1}",
                label = item.title,
                subtitle = item.subtitle ?: if (item.isUpcoming) "Coming Soon" else "Streaming Portal",
                category = MovieZoneCategory.MOVIE,
                gradientColors = gradient,
                targetUrl = item.url,
                isUpcoming = item.isUpcoming
            )
        }

    // Category 2: Anime (dynamically derived from AppConfig.animeLinks)
    val animeCards: List<MovieZoneCardItem>
        get() = AppConfig.animeLinks.mapIndexed { index, item ->
            val gradient = if (item.isUpcoming) {
                listOf(AnimeGlowGold, AnimeGlowPink)
            } else {
                gradientPalette[index % gradientPalette.size]
            }
            MovieZoneCardItem(
                id = "anime_${index + 1}",
                label = item.title,
                subtitle = item.subtitle ?: if (item.isUpcoming) "Coming Soon" else "Anime Portal",
                category = MovieZoneCategory.ANIME,
                gradientColors = gradient,
                targetUrl = item.url,
                isUpcoming = item.isUpcoming
            )
        }

    // Category 3: Manga (dynamically derived from AppConfig.mangaLinks)
    val mangaCards: List<MovieZoneCardItem>
        get() = AppConfig.mangaLinks.mapIndexed { index, item ->
            val gradient = if (item.isUpcoming) {
                listOf(AnimeGlowGold, AnimeGlowPink)
            } else {
                gradientPalette[index % gradientPalette.size]
            }
            MovieZoneCardItem(
                id = "manga_${index + 1}",
                label = item.title,
                subtitle = item.subtitle ?: if (item.isUpcoming) "Coming Soon" else "Manga Reader",
                category = MovieZoneCategory.MANGA,
                gradientColors = gradient,
                targetUrl = item.url,
                isUpcoming = item.isUpcoming
            )
        }
}

@Composable
fun MovieZoneScreen(
    onBackClick: () -> Unit,
    onOpenUrl: (title: String, url: String) -> Unit,
    onUpcomingClick: (title: String, categoryTitle: String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    // Movie is selected by default
    var selectedCategory by remember { mutableStateOf(MovieZoneCategory.MOVIE) }

    val cards = when (selectedCategory) {
        MovieZoneCategory.MOVIE -> MovieZoneRepository.movieCards
        MovieZoneCategory.ANIME -> MovieZoneRepository.animeCards
        MovieZoneCategory.MANGA -> MovieZoneRepository.mangaCards
    }

    val categoryBarBottomOffset = contentPadding.calculateBottomPadding() + 6.dp

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        // Central Scrollable Cards Grid dynamically rendered based on config size
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 680.dp),
            contentPadding = PaddingValues(
                top = contentPadding.calculateTopPadding() + 10.dp,
                bottom = categoryBarBottomOffset + 68.dp, // Ensures last row of cards is never hidden
                start = 16.dp,
                end = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Banner
            item {
                MovieZoneHeaderBanner(
                    selectedCategory = selectedCategory,
                    cardCount = cards.size,
                    onBackClick = onBackClick
                )
            }

            // Two-column responsive card grid matching Class card dimensions
            val chunkedCards = cards.chunked(2)
            items(chunkedCards.size, key = { index -> "${selectedCategory.name}_row_$index" }) { rowIndex ->
                val pair = chunkedCards[rowIndex]
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Card 1
                    MovieZoneGridCard(
                        item = pair[0],
                        modifier = Modifier.weight(1f),
                        onClick = {
                            if (pair[0].isUpcoming) {
                                onUpcomingClick(pair[0].label, selectedCategory.title)
                            } else {
                                onOpenUrl(pair[0].label, pair[0].targetUrl)
                            }
                        }
                    )

                    // Card 2 (if present)
                    if (pair.size > 1) {
                        MovieZoneGridCard(
                            item = pair[1],
                            modifier = Modifier.weight(1f),
                            onClick = {
                                if (pair[1].isUpcoming) {
                                    onUpcomingClick(pair[1].label, selectedCategory.title)
                                } else {
                                    onOpenUrl(pair[1].label, pair[1].targetUrl)
                                }
                            }
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        // Fixed Category Switching Bar immediately above the bottom navigation bar
        CategorySwitchingBar(
            selectedCategory = selectedCategory,
            onCategorySelected = { selectedCategory = it },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = categoryBarBottomOffset)
        )
    }
}

/**
 * Backward compatibility overload for MovieZoneScreen
 */
@Composable
fun MovieZoneScreen(
    onBackClick: () -> Unit,
    onCardClick: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    MovieZoneScreen(
        onBackClick = onBackClick,
        onOpenUrl = { title, url -> /* fallback if called without onOpenUrl */ },
        onUpcomingClick = { title, cat -> onCardClick(title, "Coming soon in $cat") },
        modifier = modifier,
        contentPadding = contentPadding
    )
}

@Composable
private fun MovieZoneHeaderBanner(
    selectedCategory: MovieZoneCategory,
    cardCount: Int,
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
                        .testTag("movie_zone_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back to Home",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Movie Zone",
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White.copy(alpha = 0.14f))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = selectedCategory.title,
                                color = AnimeGlowGold,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Text(
                        text = selectedCategory.description,
                        color = Color.White.copy(alpha = 0.78f),
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Showing $cardCount ${selectedCategory.title} Portals",
                    color = Color.White.copy(alpha = 0.70f),
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Tap to Stream",
                    color = AnimeGlowCyan,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

/**
 * Grid Card matching the exact 138.dp height, layout, and visual styling
 * of the existing Class 9, Class 10, Class 11, Class 12 cards.
 * If isUpcoming is true, an eye-catching "Upcoming" badge is displayed.
 */
@Composable
private fun MovieZoneGridCard(
    item: MovieZoneCardItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    GlassCard(
        modifier = modifier.height(138.dp),
        onClick = onClick,
        testTag = "movie_zone_card_${item.id}",
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
                        .background(Brush.linearGradient(item.gradientColors)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (item.isUpcoming) Icons.Rounded.AutoAwesome else item.category.icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                if (item.isUpcoming) {
                    // Eye-catching Upcoming Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(AnimeGlowGold.copy(alpha = 0.95f), AnimeGlowPink.copy(alpha = 0.95f))
                                )
                            )
                            .padding(horizontal = 7.dp, vertical = 3.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.AutoAwesome,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(10.dp)
                            )
                            Text(
                                text = "UPCOMING",
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                } else {
                    // Interactive chevron arrow
                    Icon(
                        imageVector = Icons.Rounded.ChevronRight,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.70f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Column {
                Text(
                    text = item.label,
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.3.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.subtitle,
                    color = if (item.isUpcoming) AnimeGlowGold else Color.White.copy(alpha = 0.75f),
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

/**
 * Compact horizontal category-switching bar fixed immediately above the bottom navigation bar.
 * Displays: 1. Movie  2. Anime  3. Manga in exact order.
 */
@Composable
private fun CategorySwitchingBar(
    selectedCategory: MovieZoneCategory,
    onCategorySelected: (MovieZoneCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .widthIn(max = 600.dp)
            .padding(horizontal = 24.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(14.dp, RoundedCornerShape(22.dp), spotColor = Color(0x60000000)),
            shape = RoundedCornerShape(22.dp),
            color = Color(0x800D111F),
            border = BorderStroke(
                1.dp,
                Brush.verticalGradient(
                    listOf(Color.White.copy(0.40f), Color.White.copy(0.15f))
                )
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Exact order: 1. Movie  2. Anime  3. Manga
                val categories = listOf(
                    MovieZoneCategory.MOVIE,
                    MovieZoneCategory.ANIME,
                    MovieZoneCategory.MANGA
                )

                categories.forEach { category ->
                    val isSelected = category == selectedCategory
                    CategorySwitchingTab(
                        title = category.title,
                        icon = category.icon,
                        isSelected = isSelected,
                        testTag = "category_tab_${category.name.lowercase()}",
                        modifier = Modifier.weight(1f),
                        onClick = { onCategorySelected(category) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CategorySwitchingTab(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    testTag: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val backgroundModifier = if (isSelected) {
        Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        AnimeGlowPink.copy(alpha = 0.85f),
                        AnimeGlowCyan.copy(alpha = 0.80f)
                    )
                )
            )
            .border(
                1.dp,
                Brush.horizontalGradient(
                    listOf(Color.White.copy(0.6f), Color.White.copy(0.3f))
                ),
                RoundedCornerShape(16.dp)
            )
    } else {
        Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.06f))
    }

    Row(
        modifier = modifier
            .then(backgroundModifier)
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = if (isSelected) Color.White else Color.White.copy(alpha = 0.70f),
            modifier = Modifier.size(17.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = title,
            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.75f),
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}
