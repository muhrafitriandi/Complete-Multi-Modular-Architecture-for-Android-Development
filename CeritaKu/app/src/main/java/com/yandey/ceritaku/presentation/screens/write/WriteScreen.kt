package com.yandey.ceritaku.presentation.screens.write

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.yandey.ceritaku.model.Story

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WriteScreen(
    selectedStory: Story?,
    pagerState: PagerState,
    onBackPressed: () -> Unit,
    onDeleteConfirmed: () -> Unit,
) {
    Scaffold(
        topBar = {
            WriteTopBar(
                selectedStory = selectedStory,
                onBackPressed = onBackPressed,
                onDeleteConfirmed = onDeleteConfirmed
            )
        },
        content = {
            WriteContent(
                paddingValues = it,
                title = "",
                onTitleChanged = {},
                description = "",
                onDescriptionChanged = {},
                pagerState = pagerState
            )
        },
    )
}