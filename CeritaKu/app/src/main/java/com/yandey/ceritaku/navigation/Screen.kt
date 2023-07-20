package com.yandey.ceritaku.navigation

import com.yandey.ceritaku.util.Constants.KEY_STORY_ID

sealed class Screen(val route: String) {
    object Authentication : Screen(route = "authentication_screen")
    object Home : Screen(route = "home_screen")
    object Write : Screen(route = "write_screen?$KEY_STORY_ID={$KEY_STORY_ID}") {
        fun passDiaryId(storyId: String) = "write_screen?$KEY_STORY_ID=$storyId"
    }
}
