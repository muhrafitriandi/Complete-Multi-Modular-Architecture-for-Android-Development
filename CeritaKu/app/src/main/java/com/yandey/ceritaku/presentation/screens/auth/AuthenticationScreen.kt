package com.yandey.ceritaku.presentation.screens.auth

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarState
import com.stevdzasan.onetap.OneTapSignInState
import com.stevdzasan.onetap.OneTapSignInWithGoogle
import com.yandey.ceritaku.util.Constants.CLIENT_ID
import com.yandey.deardiary.R
import java.lang.Exception

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AuthenticationScreen(
    loadingState: Boolean,
    oneTapSignInState: OneTapSignInState,
    messageBarState: MessageBarState,
    onButtonClicked: () -> Unit,
) {

    val context = LocalContext.current

    Scaffold(
        content = {
            ContentWithMessageBar(messageBarState = messageBarState) {
                AuthenticationContent(
                    loadingState = loadingState,
                    onButtonClicked = onButtonClicked
                )
            }
        }
    )

    OneTapSignInWithGoogle(
        state = oneTapSignInState,
        clientId = CLIENT_ID,
        onTokenIdReceived = { tokenId ->
            messageBarState.addSuccess(message = context.resources.getString(R.string.message_bar_successfully_authenticated))
        },
        onDialogDismissed = { message ->
            messageBarState.addError(Exception(message))
        }
    )
}