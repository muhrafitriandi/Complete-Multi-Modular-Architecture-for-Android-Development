package com.yandey.ceritaku.presentation.screens.write

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yandey.ceritaku.data.repository.MongoDB
import com.yandey.ceritaku.model.Mood
import com.yandey.ceritaku.model.Story
import com.yandey.ceritaku.util.Constants.KEY_STORY_ID
import com.yandey.ceritaku.util.Empty
import com.yandey.ceritaku.util.RequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId

class WriteViewModel(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    var uiState by mutableStateOf(UiState())
        private set

    init {
        getStoryIdArgument()
        fetchSelectedStory()
    }

    private fun getStoryIdArgument() {
        uiState = uiState.copy(
            selectedStoryId = savedStateHandle.get<String>(
                key = KEY_STORY_ID
            )
        )
    }

    private fun fetchSelectedStory() {
        if (uiState.selectedStoryId != null) {
            viewModelScope.launch(Dispatchers.Main) {
                val story = MongoDB.getSelectedStory(
                    storyId = ObjectId.invoke(uiState.selectedStoryId!!)
                )
                if (story is RequestState.Success) {
                    setSelectedStory(story = story.data)
                    setTitle(title = story.data.title)
                    setDescription(description = story.data.description)
                    setMood(mood = Mood.valueOf(story.data.mood))
                }
            }
        }
    }

    private fun setSelectedStory(story: Story) {
        uiState = uiState.copy(selectedStory = story)
    }

    fun setTitle(title: String) {
        uiState = uiState.copy(title = title)
    }

    fun setDescription(description: String) {
        uiState = uiState.copy(description = description)
    }

    private fun setMood(mood: Mood) {
        uiState = uiState.copy(mood = mood)
    }
}

data class UiState(
    val selectedStoryId: String? = null,
    val selectedStory: Story? = null,
    val title: String = String.Empty,
    val description: String = String.Empty,
    val mood: Mood = Mood.Neutral,
)