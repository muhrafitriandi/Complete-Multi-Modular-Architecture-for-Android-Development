package com.yandey.ceritaku.presentation.screens.auth

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yandey.ceritaku.presentation.components.GoogleButton
import com.yandey.ceritaku.ui.theme.Blue
import com.yandey.ceritaku.ui.theme.Green
import com.yandey.ceritaku.ui.theme.Red
import com.yandey.ceritaku.ui.theme.Yellow
import com.yandey.deardiary.R

@Composable
fun AuthenticationContent(
    loadingState: Boolean,
    onButtonClicked: () -> Unit,
) {

    val infiniteTransition = rememberInfiniteTransition()
    val scrollState = rememberScrollState()
    val colors = listOf(Red, Yellow, Green, Blue)

    val color by infiniteTransition.animateColor(
        initialValue = colors.first(),
        targetValue = colors.last(),
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                colors.forEachIndexed { index, color ->
                    durationMillis = 5000 / colors.size
                    if (index < colors.size - 1) {
                        color at ((index / (colors.size - 1).toFloat()).toInt())
                    }
                }
            },
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
                    text = "Mengabadikan Kisahmu,",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize
                )
                Text(
                    text = "Membebaskan Imajinasimu.",
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