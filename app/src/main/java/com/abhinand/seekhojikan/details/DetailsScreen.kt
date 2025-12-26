package com.abhinand.seekhojikan.details

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import com.abhinand.seekhojikan.core.navigation.Action
import com.abhinand.seekhojikan.home.domain.model.Anime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(anime: Anime, onNavigate: (Action) -> Unit) {

    Scaffold(topBar = {
        TopAppBar(title = { Text(anime.title) }, navigationIcon = {
            IconButton(onClick = { onNavigate(Action.Pop) }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
        })
    }) { }

}