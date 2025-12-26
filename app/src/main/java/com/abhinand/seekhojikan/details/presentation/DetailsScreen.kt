package com.abhinand.seekhojikan.details.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.abhinand.seekhojikan.core.navigation.Action
import com.abhinand.seekhojikan.home.domain.model.Anime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    anime: Anime,
    viewModel: DetailsViewModel = hiltViewModel(),
    onNavigate: (Action) -> Unit
) {
    val state = viewModel.state.value

    LaunchedEffect(key1 = anime.malId) {
        viewModel.getAnimeDetails(anime.malId)
    }

    Scaffold(topBar = {
        TopAppBar(title = { Text(anime.title) }, navigationIcon = {
            IconButton(onClick = { onNavigate(Action.Pop) }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
        })
    }) {
        Box(modifier = Modifier.fillMaxSize()) {
            state.animeDetails?.let { animeDetails ->
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
                        // VideoPlayer composable will be here
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Video Player Placeholder")
                        }
                    }
                    item {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = animeDetails.title,
                                style = MaterialTheme.typography.headlineMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = animeDetails.synopsis,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Genres: ${animeDetails.genres.joinToString()}",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Episodes: ${animeDetails.episodes}",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Rating: ${animeDetails.rating}",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Score: ${animeDetails.score}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
            if (state.error != null) {
                Text(
                    text = state.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}