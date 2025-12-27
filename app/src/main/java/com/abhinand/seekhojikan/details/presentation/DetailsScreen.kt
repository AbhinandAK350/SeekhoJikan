package com.abhinand.seekhojikan.details.presentation

import android.content.Intent
import android.net.Uri
import android.util.Log
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    val context = LocalContext.current

    LaunchedEffect(key1 = anime.malId) {
        viewModel.getAnimeDetails(anime.malId)
    }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(anime.title, maxLines = 1, overflow = TextOverflow.Ellipsis) },
            navigationIcon = {
                IconButton(onClick = { onNavigate(Action.Pop) }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            })
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
            } else if (state.error != null) {
                Text(
                    text = state.error,
                )
            } else {
                state.animeDetails?.let { animeDetails ->
                    var showConfirmationDialog by remember { mutableStateOf(false) }

                    if (showConfirmationDialog) {
                        val videoId = Util.extractYouTubeVideoId(animeDetails.embeddedUrl ?: "")
                        AlertDialog(
                            onDismissRequest = { showConfirmationDialog = false },
                            title = { Text("Watch Trailer") },
                            text = { Text("You are about to be redirected to YouTube to watch the trailer. Do you want to continue?") },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        showConfirmationDialog = false
                                        val intent = Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("https://www.youtube.com/watch?v=$videoId")
                                        )
                                        context.startActivity(intent)
                                    }
                                ) {
                                    Text("Confirm")
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = { showConfirmationDialog = false }
                                ) {
                                    Text("Cancel")
                                }
                            }
                        )
                    }

                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        item {
                            var showPoster by remember { mutableStateOf(false) }

                            if (animeDetails.embeddedUrl.isNullOrEmpty() || showPoster || !state.isOnline) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(350.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (FeatureFlags.SHOW_IMAGES) {
                                        GlideImage(
                                            model = animeDetails.imageUrl,
                                            contentDescription = animeDetails.title,
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop,
                                            loading = placeholder {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                        .background(Color.Gray),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    CircularProgressIndicator()
                                                }
                                            },
                                            failure = placeholder {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                        .background(Color.Gray),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.BrokenImage,
                                                        contentDescription = "Image unavailable",
                                                        tint = Color.White
                                                    )
                                                }
                                            }
                                        )
                                    } else {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(Color.Gray),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.BrokenImage,
                                                contentDescription = "Image unavailable",
                                                tint = Color.White
                                            )
                                        }
                                    }

                                    if (showPoster && !animeDetails.embeddedUrl.isNullOrEmpty()) {
                                        val videoId =
                                            Util.extractYouTubeVideoId(animeDetails.embeddedUrl)
                                        if (videoId != null) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .clickable { showConfirmationDialog = true },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .clip(CircleShape)
                                                        .background(Color.Black.copy(alpha = 0.6f))
                                                        .padding(8.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.PlayArrow,
                                                        contentDescription = "Play Video",
                                                        modifier = Modifier.size(48.dp),
                                                        tint = Color.White
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                val lifecycleOwner = LocalLifecycleOwner.current
                                val youtubePlayerView = remember {
                                    YouTubePlayerView(context).apply {
                                        lifecycleOwner.lifecycle.addObserver(this)
                                    }
                                }

                                var playerHasError by remember { mutableStateOf(false) }

                                LaunchedEffect(playerHasError) {
                                    if (playerHasError) {
                                        delay(1000)
                                        showPoster = true
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
                                ) {
                                    it.addYouTubePlayerListener(object :
                                        AbstractYouTubePlayerListener() {
                                        override fun onReady(youTubePlayer: YouTubePlayer) {
                                            youTubePlayer.cueVideo(
                                                Util.extractYouTubeVideoId(
                                                    animeDetails.embeddedUrl
                                                ) ?: "", 0f
                                            )
                                        }

                                        override fun onError(
                                            youTubePlayer: YouTubePlayer,
                                            error: PlayerConstants.PlayerError
                                        ) {
                                            super.onError(youTubePlayer, error)
                                            Log.e("YouTubePlayer", "Error: $error")
                                            playerHasError = true
                                        }
                                    })
                                }
                            }
                        }
                        item {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = animeDetails.title,
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                ExpandableText(
                                    text = animeDetails.synopsis ?: "No synopsis available.",
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                DetailItem(
                                    icon = Icons.Default.Movie,
                                    text = "Episodes: ${animeDetails.episodes}"
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                DetailItem(
                                    icon = Icons.Default.Star,
                                    text = "Score: ${animeDetails.score}"
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
                                    verticalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    animeDetails.genres.forEach { genre ->
                                        GenreChip(genre = genre)
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
    }
}

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

@Composable
fun GenreChip(genre: NamedResourceDto) {
    AssistChip(
        onClick = { },
        label = { Text(text = genre.name) })
}

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
