package com.yandey.ceritaku.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import com.yandey.ceritaku.presentation.screens.auth.AuthenticationScreen
import com.yandey.ceritaku.util.Constants.KEY_DIARY_ID

@Composable
fun NavGraph(startDestination: String, navHostController: NavHostController) {
    NavHost(startDestination = startDestination, navController = navHostController) {
        authenticationRoute()
        homeRoute()
        writeRoute()
    }
}

fun NavGraphBuilder.authenticationRoute() {
    composable(route = Screen.Authentication.route) {
        val oneTapSignInState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()

        AuthenticationScreen(
            loadingState = oneTapSignInState.opened,
            oneTapSignInState = oneTapSignInState,
            messageBarState = messageBarState,
            onButtonClicked = {
                oneTapSignInState.open()
            }
        )
    }
}

fun NavGraphBuilder.homeRoute() {
    composable(route = Screen.Home.route) {

    }
}

fun NavGraphBuilder.writeRoute() {
    composable(
        route = Screen.Write.route, arguments = listOf(navArgument(name = KEY_DIARY_ID) {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        })
    ) {

    }
}
