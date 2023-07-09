package com.yandey.ceritaku

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.yandey.ceritaku.navigation.NavGraph
import com.yandey.ceritaku.navigation.Screen
import com.yandey.ceritaku.ui.theme.DearDiaryTheme
import com.yandey.ceritaku.util.Constants.APP_ID
import io.realm.kotlin.mongodb.App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            DearDiaryTheme {
                val navController = rememberNavController()
                NavGraph(
                    startDestination = getStartDestination(),
                    navHostController = navController
                )
            }
        }
    }

    private fun getStartDestination(): String {
        val user = App.create(APP_ID).currentUser

        return if (user != null && user.loggedIn) Screen.Home.route
        else Screen.Authentication.route
    }
}