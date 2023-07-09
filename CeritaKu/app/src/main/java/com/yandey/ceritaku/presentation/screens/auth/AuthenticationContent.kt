package com.yandey.ceritaku.presentation.screens.auth

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yandey.ceritaku.presentation.components.GoogleButton
import com.yandey.deardiary.R
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yandey.ceritaku.util.Constants.COLOR_ANIMATION_DURATION_MILLIS

@Composable
fun AuthenticationContent(
    loadingState: Boolean,
    onButtonClicked: () -> Unit,
) {
    val scrollState = rememberScrollState()

    val viewModel: AuthenticationViewModel = viewModel()

    val targetColor by viewModel::currentColor

    val color by animateColorAsState(
        targetValue = targetColor,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = COLOR_ANIMATION_DURATION_MILLIS,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .weight(9f)
                .fillMaxWidth()
                .padding(all = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .weight(weight = 10f)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Spacer(modifier = Modifier.height(64.dp))
                Image(
                    modifier = Modifier
                        .size(100.dp)
                        .fillMaxWidth(),
                    painter = painterResource(id = R.drawable.ceritaku_logo),
                    contentDescription = "CeritaKu Logo"
                )
                Image(
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth(),
                    alignment = Alignment.TopStart,
                    painter = painterResource(id = R.drawable.ceritaku_logo_text),
                    contentDescription = "CeritaKu Logo"
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(id = R.string.text_slogan_1),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize
                )
                Text(
                    text = stringResource(id = R.string.text_slogan_2),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            Column(
                modifier = Modifier.weight(weight = 2f),
                verticalArrangement = Arrangement.Bottom
            ) {
                GoogleButton(
                    backgroundColor = color,
                    loadingState = loadingState,
                    onClick = onButtonClicked
                )
            }
        }
    }
}

@Composable
@Preview
fun AuthenticationContentPreview() {
    AuthenticationContent(loadingState = false) {

    }
}