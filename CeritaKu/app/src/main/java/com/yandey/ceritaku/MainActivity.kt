package com.yandey.ceritaku

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.yandey.ceritaku.navigation.NavGraph
import com.yandey.ceritaku.navigation.Screen
import com.yandey.ceritaku.ui.theme.DearDiaryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            DearDiaryTheme {
                val navController = rememberNavController()
                NavGraph(startDestination = Screen.Authentication.route, navHostController = navController)
            }
        }
    }
}