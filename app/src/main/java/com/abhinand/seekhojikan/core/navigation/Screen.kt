package com.abhinand.seekhojikan.core.navigation

import androidx.navigation3.runtime.NavKey
import com.abhinand.seekhojikan.home.domain.model.Anime
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen: NavKey {

    @Serializable
    object Home: Screen()

    @Serializable
    data class Details(val anime: Anime) : Screen()

}