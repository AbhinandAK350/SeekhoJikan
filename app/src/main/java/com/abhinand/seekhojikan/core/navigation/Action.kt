package com.abhinand.seekhojikan.core.navigation

sealed class Action {

    object Pop : Action()

    data class Push(val screen: Screen) : Action()
}