package com.yandey.ceritaku.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.yandey.ceritaku.util.Constants.APP_ID
import com.yandey.ceritaku.util.Constants.KEY_DIARY_ID
import com.yandey.deardiary.R
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.launch
import java.lang.Exception

@Composable
fun NavGraph(startDestination: String, navHostController: NavHostController) {
    NavHost(startDestination = startDestination, navController = navHostController) {
        authenticationRoute(navigateToHome = {
            with(navHostController) {
                popBackStack()
                navigate(Screen.Home.route)
            }
        })
        homeRoute()
        writeRoute()
    }
}

fun NavGraphBuilder.authenticationRoute(
    navigateToHome: () -> Unit,
) {
    composable(route = Screen.Authentication.route) {
        val viewModel: AuthenticationViewModel = viewModel()
        val loadingState by viewModel.loadingState
        val authenticated by viewModel.authenticated
        val oneTapSignInState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()
        val context = LocalContext.current

        AuthenticationScreen(
            authenticated = authenticated,
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
                        if (it) messageBarState.addSuccess(message = context.resources.getString(R.string.message_bar_successfully_authenticated))
                        viewModel.setLoading(false)
                    },
                    onError = {
                        messageBarState.addError(it)
                        viewModel.setLoading(false)
                    }
                )
            },
            onDialogDismissed = { message ->
                messageBarState.addError(Exception(message))
                viewModel.setLoading(false)
            },
            navigateToHome = navigateToHome
        )
    }
}

fun NavGraphBuilder.homeRoute() {
    composable(route = Screen.Home.route) {
        val scope = rememberCoroutineScope()
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {
                scope.launch {
                    App.Companion.create(APP_ID).currentUser?.logOut()
                }
            }) {
                Text(text = "Logout")
            }
        }
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
