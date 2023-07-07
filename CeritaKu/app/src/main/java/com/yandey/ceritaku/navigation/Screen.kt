package com.yandey.ceritaku.navigation

import com.yandey.ceritaku.util.Constants.KEY_DIARY_ID

sealed class Screen(val route: String) {
    object Authentication : Screen(route = "authentication_screen")
    object Home : Screen(route = "home_screen")
    object Write : Screen(route = "write_screen?$KEY_DIARY_ID={$KEY_DIARY_ID}") {
        fun passDiaryId(diaryId: String) = "write_screen?$KEY_DIARY_ID=$diaryId"
    }
}
