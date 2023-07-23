package com.yandey.ceritaku.presentation.screens.write

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarPosition
import com.stevdzasan.messagebar.MessageBarState
import com.yandey.ceritaku.model.Mood
import com.yandey.ceritaku.model.Story

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WriteScreen(
    messageBarState: MessageBarState,
    uiState: UiState,
    pagerState: PagerState,
    moodName: () -> String,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onBackPressed: () -> Unit,
    onDeleteConfirmed: () -> Unit,
    onSaveClicked: (Story) -> Unit,
    onFieldError: (String) -> Unit,
) {
    LaunchedEffect(key1 = uiState.mood) {
        pagerState.scrollToPage(Mood.valueOf(uiState.mood.name).ordinal)
    }

    Scaffold(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
            .navigationBarsPadding(),
        content = {
            ContentWithMessageBar(
                messageBarState = messageBarState,
                position = MessageBarPosition.BOTTOM
            ) {
                WriteContent(
                    uiState = uiState,
                    paddingValues = it,
                    title = uiState.title,
                    onTitleChanged = onTitleChanged,
                    description = uiState.description,
                    onDescriptionChanged = onDescriptionChanged,
                    pagerState = pagerState,
                    onSaveClicked = onSaveClicked,
                    onFieldError = onFieldError
                )
            }
        },
        topBar = {
            WriteTopBar(
                selectedStory = uiState.selectedStory,
                onBackPressed = onBackPressed,
                onDeleteConfirmed = onDeleteConfirmed,
                moodName = moodName
            )
        },
    )
}