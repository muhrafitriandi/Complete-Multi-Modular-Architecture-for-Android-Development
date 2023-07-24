package com.yandey.ceritaku.presentation.screens.write

import android.content.Context
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
import com.yandey.ceritaku.util.toRealmInstant
import com.yandey.deardiary.R
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId
import java.time.ZonedDateTime

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
                MongoDB.getSelectedStory(
                    storyId = ObjectId.invoke(uiState.selectedStoryId!!)
                ).collect { story ->
                    if (story is RequestState.Success) {
                        setSelectedStory(story = story.data)
                        setTitle(title = story.data.title)
                        setDescription(description = story.data.description)
                        setMood(mood = Mood.valueOf(story.data.mood))
                    }
                }
            }
        }
    }

    private suspend fun insertStory(
        story: Story,
        context: Context,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
    ) {
        val data = MongoDB.insertStory(story = story.apply {
            if (uiState.updatedDateTime != null)
                date = uiState.updatedDateTime!!
        })
        if (data is RequestState.Success) {
            withContext(Dispatchers.Main) {
                onSuccess(context.getString(R.string.message_bar_successfully_added_story))
            }
        } else if (data is RequestState.Error) {
            withContext(Dispatchers.Main) {
                onError(data.error.message.toString())
            }
        }
    }

    private suspend fun updateStory(
        story: Story,
        context: Context,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
    ) {
        val data = MongoDB.updateStory(
            story = story.apply {
                id = ObjectId.invoke(uiState.selectedStoryId!!)
                date =
                    if (uiState.updatedDateTime != null) uiState.updatedDateTime!! else uiState.selectedStory!!.date
            },
            context = context
        )
        if (data is RequestState.Success) {
            withContext(Dispatchers.Main) {
                onSuccess(context.getString(R.string.message_bar_successfully_updated_story))
            }
        } else if (data is RequestState.Error) {
            withContext(Dispatchers.Main) {
                onError(data.error.message.toString())
            }
        }
    }

    fun upsertStory(
        story: Story,
        context: Context,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            if (uiState.selectedStoryId != null) {
                updateStory(
                    story = story,
                    context = context,
                    onSuccess = onSuccess,
                    onError = onError
                )
            } else {
                insertStory(
                    story = story,
                    context = context,
                    onSuccess = onSuccess,
                    onError = onError
                )
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

    fun setUpdatedDateTime(zonedDateTime: ZonedDateTime) {
        uiState = uiState.copy(updatedDateTime = zonedDateTime.toInstant().toRealmInstant())
    }
}

data class UiState(
    val selectedStoryId: String? = null,
    val selectedStory: Story? = null,
    val title: String = String.Empty,
    val description: String = String.Empty,
    val mood: Mood = Mood.Neutral,
    val updatedDateTime: RealmInstant? = null,
)