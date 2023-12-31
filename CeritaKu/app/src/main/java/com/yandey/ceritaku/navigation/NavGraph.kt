package com.yandey.ceritaku.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import com.yandey.ceritaku.model.Mood
import com.yandey.ceritaku.presentation.components.DisplayAlertDialog
import com.yandey.ceritaku.presentation.screens.auth.AuthenticationScreen
import com.yandey.ceritaku.presentation.screens.auth.AuthenticationViewModel
import com.yandey.ceritaku.presentation.screens.home.HomeScreen
import com.yandey.ceritaku.presentation.screens.home.HomeViewModel
import com.yandey.ceritaku.presentation.screens.write.WriteScreen
import com.yandey.ceritaku.presentation.screens.write.WriteViewModel
import com.yandey.ceritaku.util.Constants.APP_ID
import com.yandey.ceritaku.util.Constants.KEY_STORY_ID
import com.yandey.ceritaku.util.RequestState
import com.yandey.deardiary.R
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun NavGraph(
    startDestination: String,
    navHostController: NavHostController,
    onDataLoaded: () -> Unit,
) {
    NavHost(startDestination = startDestination, navController = navHostController) {
        authenticationRoute(
            navigateToHome = {
                with(navHostController) {
                    popBackStack()
                    navigate(Screen.Home.route)
                }
            },
            onDataLoaded = onDataLoaded
        )
        homeRoute(
            navigateToWrite = {
                navHostController.navigate(Screen.Write.route)
            },
            navigateToAuthentication = {
                with(navHostController) {
                    popBackStack()
                    navigate(Screen.Authentication.route)
                }
            },
            onDataLoaded = onDataLoaded,
            navigateToWriteWithArgs = {
                navHostController.navigate(Screen.Write.passDiaryId(storyId = it))
            }
        )
        writeRoute(
            onBackPressed = {
                with(navHostController) {
                    popBackStack()
                }
            }
        )
    }
}

fun NavGraphBuilder.authenticationRoute(
    navigateToHome: () -> Unit,
    onDataLoaded: () -> Unit,
) {
    composable(route = Screen.Authentication.route) {
        val viewModel: AuthenticationViewModel = viewModel()
        val loadingState by viewModel.loadingState
        val authenticated by viewModel.authenticated
        val oneTapSignInState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()
        val context = LocalContext.current

        LaunchedEffect(key1 = Unit) {
            onDataLoaded()
        }

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
                        messageBarState.addSuccess(message = context.resources.getString(R.string.message_bar_successfully_authenticated))
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

fun NavGraphBuilder.homeRoute(
    navigateToWrite: () -> Unit,
    navigateToWriteWithArgs: (String) -> Unit,
    navigateToAuthentication: () -> Unit,
    onDataLoaded: () -> Unit,
) {
    composable(route = Screen.Home.route) {
        val viewModel: HomeViewModel = viewModel()
        val stories by viewModel.stories
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        var signOutDialogOpened by remember { mutableStateOf(false) }

        LaunchedEffect(key1 = stories) {
            if (stories !is RequestState.Loading) {
                onDataLoaded()
            }
        }

        HomeScreen(
            stories = stories,
            onMenuClicked = {
                scope.launch {
                    drawerState.open()
                }
            },
            navigateToWrite = navigateToWrite,
            drawerState = drawerState,
            onSignOutClicked = {
                signOutDialogOpened = true
            },
            navigateToWriteWithArgs = navigateToWriteWithArgs
        )

        DisplayAlertDialog(
            titles = stringResource(id = R.string.text_sign_out_title),
            message = stringResource(id = R.string.text_sign_out_description),
            isDialogOpened = signOutDialogOpened,
            onClosedDialog = {
                signOutDialogOpened = false
            },
            onYesClicked = {
                scope.launch(Dispatchers.IO) {
                    try {
                        App.create(APP_ID).currentUser?.logOut()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    withContext(Dispatchers.Main) {
                        navigateToAuthentication()
                    }
                }
            }
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
fun NavGraphBuilder.writeRoute(
    onBackPressed: () -> Unit,
) {

    composable(
        route = Screen.Write.route, arguments = listOf(navArgument(name = KEY_STORY_ID) {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        })
    ) {
        val hostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()
        val context = LocalContext.current
        val viewModel: WriteViewModel = viewModel()
        val uiState = viewModel.uiState
        val pagerState = rememberPagerState(pageCount = {
            Mood.values().size
        })
        val pageNumber by remember {
            derivedStateOf {
                pagerState.currentPage
            }
        }
        WriteScreen(
            uiState = uiState,
            hostState = hostState,
            onBackPressed = onBackPressed,
            onDeleteConfirmed = {},
            pagerState = pagerState,
            onTitleChanged = { viewModel.setTitle(title = it) },
            onDescriptionChanged = { viewModel.setDescription(description = it) },
            moodName = { Mood.values()[pageNumber].getLocalizedMood(context) },
            onSaveClicked = {
                viewModel.upsertStory(
                    story = it.apply {
                        this.mood = Mood.values()[pageNumber].name
                    },
                    onSuccess = {
                        onBackPressed()
                    },
                    onError = { errorMessage ->
                        scope.launch {
                            with(hostState) {
                                currentSnackbarData?.dismiss()
                                showSnackbar(message = errorMessage, withDismissAction = true)
                            }
                        }
                    },
                    context = context
                )
            },
            onFieldError = {
                scope.launch {
                    with(hostState) {
                        currentSnackbarData?.dismiss()
                        showSnackbar(message = it, withDismissAction = true)
                    }
                }
            },
            onDateTimeUpdated = {
                viewModel.setUpdatedDateTime(it)
            }
        )
    }
}
