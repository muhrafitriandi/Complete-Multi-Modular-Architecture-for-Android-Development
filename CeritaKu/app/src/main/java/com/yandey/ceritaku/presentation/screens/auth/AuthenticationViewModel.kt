package com.yandey.ceritaku.presentation.screens.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yandey.ceritaku.ui.theme.Blue
import com.yandey.ceritaku.ui.theme.Green
import com.yandey.ceritaku.ui.theme.Red
import com.yandey.ceritaku.ui.theme.Yellow
import com.yandey.ceritaku.util.Constants.APP_ID
import com.yandey.ceritaku.util.Constants.COLOR_ANIMATION_DURATION_MILLIS
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthenticationViewModel : ViewModel() {

    private val colors = listOf(Red, Yellow, Green, Blue)
    private var currentIndex by mutableIntStateOf(0)
    var currentColor by mutableStateOf(colors[currentIndex])
        private set

    init {
        startAnimation()
    }

    private fun startAnimation() {
        viewModelScope.launch {
            while (true) {
                delay(COLOR_ANIMATION_DURATION_MILLIS.toLong())
                currentIndex = (currentIndex + 1) % colors.size
                currentColor = colors[currentIndex]
            }
        }
    }

    var authenticated = mutableStateOf(false)
        private set

    var loadingState = mutableStateOf(false)
        private set

    fun setLoading(loading: Boolean) {
        loadingState.value = loading
    }

    fun signInWithMongoAtlas(
        tokenId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit,
    ) {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    App.create(APP_ID).login(
                        Credentials.jwt(
                            jwtToken = tokenId
                        )
                    ).loggedIn
                }
                withContext(Dispatchers.Main) {
                    if (result) {
                        onSuccess()
                        delay(600)
                        authenticated.value = true
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError(e)
                }
            }
        }
    }
}