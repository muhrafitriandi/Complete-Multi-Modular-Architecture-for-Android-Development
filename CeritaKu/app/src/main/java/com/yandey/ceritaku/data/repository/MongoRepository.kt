package com.yandey.ceritaku.data.repository

import com.yandey.ceritaku.model.Story
import com.yandey.ceritaku.util.RequestState
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId
import java.time.LocalDate

typealias Stories = RequestState<Map<LocalDate, List<Story>>>

interface MongoRepository {
    fun configureTheRealm()
    fun getAllStories(): Flow<Stories>
    fun getSelectedStory(storyId: ObjectId): RequestState<Story>
}