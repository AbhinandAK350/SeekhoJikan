package com.abhinand.seekhojikan.details.presentation

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.abhinand.seekhojikan.core.config.FeatureFlags
import com.abhinand.seekhojikan.core.navigation.Action
import com.abhinand.seekhojikan.details.domain.model.AnimeDetails
import com.abhinand.seekhojikan.details.util.Util
import com.abhinand.seekhojikan.home.data.remote.dto.NamedResourceDto
import com.abhinand.seekhojikan.home.domain.model.Anime
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun DetailsScreen(
    anime: Anime,
    viewModel: DetailsViewModel = hiltViewModel(),
    onNavigate: (Action) -> Unit
) {
    val state = viewModel.state.value

    // Fetch anime details when the screen is first composed
    LaunchedEffect(key1 = anime.malId) {
        viewModel.getAnimeDetails(anime.malId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(anime.title, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                navigationIcon = {
                    IconButton(onClick = { onNavigate(Action.Pop) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            val animeDetails = state.animeDetails
            val isOnline = state.isOnline
            val context = LocalContext.current
            val lifecycleOwner = LocalLifecycleOwner.current
            var showPoster by rememberSaveable { mutableStateOf(false) }
            var playerHasError by rememberSaveable { mutableStateOf(false) }

            LaunchedEffect(playerHasError) {
                if (playerHasError) {
                    delay(500)
                    showPoster = true
                }
            }

            val shouldShowPoster =
                animeDetails?.embeddedUrl.isNullOrEmpty() || showPoster || !isOnline

            if (shouldShowPoster) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    item {
                        PosterView(
                            imageUrl = animeDetails?.imageUrl,
                            hasTrailer = !animeDetails?.embeddedUrl.isNullOrEmpty(),
                            onPlayClicked = {
                                showPoster = false
                                playerHasError = false
                            }
                        )
                    }
                    item {
                        DetailsContent(animeDetails)
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    val youtubePlayerView = remember {
                        YouTubePlayerView(context).apply {
                            lifecycleOwner.lifecycle.addObserver(this)
                        }
                    }

                    DisposableEffect(lifecycleOwner) {
                        onDispose {
                            lifecycleOwner.lifecycle.removeObserver(youtubePlayerView)
                        }
                    }

                    AndroidView(
                        factory = { youtubePlayerView },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                    ) { view ->
                        view.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {

                            override fun onReady(player: YouTubePlayer) {
                                player.cueVideo(
                                    Util.extractYouTubeVideoId(animeDetails.embeddedUrl) ?: "",
                                    0f
                                )
                            }

                            override fun onError(
                                player: YouTubePlayer,
                                error: PlayerConstants.PlayerError
                            ) {
                                playerHasError = true
                            }
                        })
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item {
                            DetailsContent(animeDetails)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PosterView(
    imageUrl: String?,
    hasTrailer: Boolean,
    onPlayClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp),
        contentAlignment = Alignment.Center
    ) {

        if (FeatureFlags.SHOW_IMAGES && !imageUrl.isNullOrEmpty()) {
            GlideImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                loading = placeholder { PosterPlaceholder() },
                failure = placeholder {
                    PosterPlaceholder()
                }
            )
        } else {
            PosterPlaceholder()
        }

        if (hasTrailer) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onPlayClicked() },
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.6f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun PosterPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.BrokenImage,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(48.dp)
        )
    }
}

@Composable
fun DetailsContent(animeDetails: AnimeDetails?) {
    Column(modifier = Modifier.padding(16.dp)) {

        Text(
            text = animeDetails?.title ?: "N/A",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        ExpandableText(
            text = animeDetails?.synopsis ?: "No synopsis available."
        )

        Spacer(modifier = Modifier.height(16.dp))

        DetailItem(
            icon = Icons.Default.Movie,
            text = "Episodes: ${animeDetails?.episodes ?: "N/A"}"
        )

        Spacer(modifier = Modifier.height(8.dp))

        DetailItem(
            icon = Icons.Default.Star,
            text = "Score: ${animeDetails?.score ?: "N/A"}"
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Genres",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            animeDetails?.genres?.forEach {
                GenreChip(it)
            }
        }
    }
}


/**
 * A composable that displays a detail item with an icon and text.
 */
@Composable
fun DetailItem(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.padding(horizontal = 4.dp))
        Text(text = text, style = MaterialTheme.typography.bodyMedium)
    }
}

/**
 * A composable that displays a genre chip.
 */
@Composable
fun GenreChip(genre: NamedResourceDto) {
    AssistChip(
        onClick = { },
        label = { Text(text = genre.name) })
}

/**
 * A composable that displays text that can be expanded and collapsed.
 */
@Composable
fun ExpandableText(text: String) {
    var isExpanded by remember { mutableStateOf(false) }
    var showToggle by remember(text) { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .animateContentSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                enabled = showToggle
            ) { isExpanded = !isExpanded }
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = if (isExpanded) Int.MAX_VALUE else 5,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = {
                if (!showToggle) {
                    showToggle = it.hasVisualOverflow
                }
            }
        )

        if (showToggle) {
            Text(
                text = if (isExpanded) "Show less" else "Show more",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 4.dp)
            )
        }
    }
}
