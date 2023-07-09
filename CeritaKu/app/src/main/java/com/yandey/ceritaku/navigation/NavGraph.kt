package com.yandey.ceritaku.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import com.yandey.ceritaku.presentation.screens.auth.AuthenticationScreen
import com.yandey.ceritaku.presentation.screens.auth.AuthenticationViewModel
import com.yandey.ceritaku.util.Constants.KEY_DIARY_ID
import com.yandey.deardiary.R
import java.lang.Exception

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
        val viewModel: AuthenticationViewModel = viewModel()
        val loadingState by viewModel.loadingState
        val oneTapSignInState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()
        val context = LocalContext.current

        AuthenticationScreen(
            loadingState = loadingState,
            oneTapSignInState = oneTapSignInState,
            messageBarState = messageBarState,
            onButtonClicked = {
                oneTapSignInState.open()
                viewModel.setLoading(true)
            },
            onTokenIdReceived = { tokenId ->
                viewModel.signInWithMongoAtlas(
                    tokenId = tokenId,
                    onSuccess = {
                        if (it) {
                            messageBarState.addSuccess(message = context.resources.getString(R.string.message_bar_successfully_authenticated))
                            viewModel.setLoading(false)
                        }
                    },
                    onError = {
                        messageBarState.addError(it)
                        viewModel.setLoading(false)
                    }
                )
            },
            onDialogDismissed = { message ->
                messageBarState.addError(Exception(message))
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
