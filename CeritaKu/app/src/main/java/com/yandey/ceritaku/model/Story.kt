package com.yandey.ceritaku.model

import com.yandey.ceritaku.util.Empty
import com.yandey.ceritaku.util.Zero
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PersistedName
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Story : RealmObject {
    @PersistedName("_id")
    @PrimaryKey
    var id: ObjectId = ObjectId()

    var ownerId: String = String.Empty

    var mood: String = Mood.Neutral.name

    var title: String = String.Empty

    var description: String = String.Empty

    var images: RealmList<String> = realmListOf()

    var date: RealmInstant = RealmInstant.from(System.currentTimeMillis(), Int.Zero)
}