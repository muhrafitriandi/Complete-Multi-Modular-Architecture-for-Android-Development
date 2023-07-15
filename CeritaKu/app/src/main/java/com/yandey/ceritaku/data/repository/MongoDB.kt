package com.yandey.ceritaku.data.repository

import com.yandey.ceritaku.model.Story
import com.yandey.ceritaku.util.Constants.APP_ID
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration

object MongoDB: MongoRepository {

    private lateinit var realm: Realm
    private val app: App = App.create(APP_ID)
    private val user = app.currentUser

    override fun configureTheRealm() {
        if (user != null) {
            val config = SyncConfiguration.Builder(user, setOf(Story::class))
                .initialSubscriptions { sub ->
                    add(
                        query = sub.query("ownerId == $0", user.id),
                        name = "User's Story"
                    )
                }
                .log(LogLevel.ALL)
                .build()
            realm = Realm.open(config)
        }
    }
}