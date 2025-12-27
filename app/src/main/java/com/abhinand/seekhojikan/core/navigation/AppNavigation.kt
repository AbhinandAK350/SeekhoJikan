package com.abhinand.seekhojikan.core.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.abhinand.seekhojikan.details.presentation.DetailsScreen
import com.abhinand.seekhojikan.home.presentation.HomeScreen

@Composable
fun AppNavigation() {

    val backStack = rememberNavBackStack(Screen.Home)

    fun onBackPress() {
        if (backStack.size > 1) {
            backStack.removeLastOrNull()
        }
    }

    fun handleNavigation(action: Action) {
        when (action) {
            is Action.Pop -> onBackPress()
            is Action.Push -> backStack.add(action.screen)
        }
    }

    NavDisplay(
        backStack = backStack,
        onBack = { onBackPress() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ), entryProvider = entryProvider {
            entry<Screen.Home> {
                HomeScreen(onNavigate = { action ->
                    handleNavigation(action = action)
                })
            }

            entry<Screen.Details> {
                DetailsScreen(anime = it.anime, onNavigate = { action ->
                    handleNavigation(action)
                })
            }
        }, predictivePopTransitionSpec = {
            EnterTransition.None togetherWith ExitTransition.None
        }
    )
}