package com.yandey.ceritaku.presentation.screens.write

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.yandey.ceritaku.model.Mood
import com.yandey.ceritaku.util.Constants.KEY_STORY_ID
import com.yandey.ceritaku.util.Empty

class WriteViewModel(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    var uiState by mutableStateOf(UiState())
        private set

    init {
        getStoryIdArgument()
    }

    private fun getStoryIdArgument() {
        uiState = uiState.copy(
            selectedDiaryId = savedStateHandle.get<String>(
                key = KEY_STORY_ID
            )
        )
    }
}

data class UiState(
    val selectedDiaryId: String? = null,
    val title: String = String.Empty,
    val description: String = String.Empty,
    val mood: Mood = Mood.Neutral,
)