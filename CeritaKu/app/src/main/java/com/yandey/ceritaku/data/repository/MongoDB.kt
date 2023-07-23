package com.yandey.ceritaku.data.repository

import android.content.res.Resources
import com.yandey.ceritaku.model.Story
import com.yandey.ceritaku.util.Constants.APP_ID
import com.yandey.ceritaku.util.RequestState
import com.yandey.ceritaku.util.toInstant
import com.yandey.deardiary.R
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId
import java.time.ZoneId

object MongoDB : MongoRepository {

    private lateinit var realm: Realm
    private val app: App = App.create(APP_ID)
    private val user = app.currentUser

    init {
        configureTheRealm()
    }

    override fun configureTheRealm() {
        if (user != null) {
            val config = SyncConfiguration.Builder(user, setOf(Story::class))
                .initialSubscriptions { sub ->
                    add(
                        query = sub.query<Story>("ownerId == $0", user.id),
                        name = "User's Story"
                    )
                }
                .log(LogLevel.ALL)
                .build()
            realm = Realm.open(config)
        }
    }

    override fun getAllStories(): Flow<Stories> {
        return if (user != null) {
            try {
                realm.query<Story>(query = "ownerId == $0", user.id)
                    .sort(property = "date", sortOrder = Sort.DESCENDING)
                    .asFlow()
                    .map { result ->
                        RequestState.Success(
                            data = result.list.groupBy { story ->
                                story.date
                                    .toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                            }
                        )
                    }

            } catch (e: Exception) {
                flow {
                    emit(RequestState.Error(e))
                }
            }
        } else {
            flow {
                emit(RequestState.Error(UserNotAuthenticatedException()))
            }
        }
    }

    override fun getSelectedStory(storyId: ObjectId): RequestState<Story> {
        return if (user != null) {
            try {
                val story = realm.query<Story>(query = "_id == $0", storyId).find().first()
                RequestState.Success(data = story)
            } catch (e: Exception) {
                RequestState.Error(e)
            }
        } else {
            RequestState.Error(UserNotAuthenticatedException())
        }
    }

    override suspend fun addNewStory(story: Story): RequestState<Story> {
        return if (user != null) {
            realm.write {
                try {
                    val addedStory = copyToRealm(story.apply { ownerId = user.id })
                    RequestState.Success(data = addedStory)
                } catch (e: Exception) {
                    RequestState.Error(e)
                }
            }
        } else {
            RequestState.Error(UserNotAuthenticatedException())
        }
    }
}

private class UserNotAuthenticatedException :
    Exception(Resources.getSystem().getString(R.string.exception_message_user_not_authenticated))