package com.yandey.ceritaku.presentation.screens.home

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.yandey.deardiary.R

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    onMenuClicked: () -> Unit,
    navigateToWrite: () -> Unit,
) {
    Scaffold(
        topBar = {
            HomeTopBar(onMenuClicked = onMenuClicked)
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = {
                    Text(text = stringResource(id = R.string.text_add_story))
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "New Story Icon"
                    )
                },
                onClick = navigateToWrite
            )
        },
        content = {

        }
    )
}