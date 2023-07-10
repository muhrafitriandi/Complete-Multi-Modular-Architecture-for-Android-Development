package com.yandey.ceritaku.presentation.components


import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.yandey.deardiary.R

@Composable
fun DisplayAlertDialog(
    titles: String,
    message: String,
    isDialogOpened: Boolean,
    onClosedDialog: () -> Unit,
    onYesClicked: () -> Unit,
) {
    if (isDialogOpened) {
        AlertDialog(
            title = {
                Text(
                    text = titles,
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = message,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontWeight = FontWeight.Bold
                )
            },
            confirmButton = {
                Button(onClick = {
                    onYesClicked()
                    onClosedDialog()
                }) {
                    Text(
                        text = stringResource(id = R.string.text_yes)
                    )
                }
            },
            dismissButton = {
                OutlinedButton(onClick = onClosedDialog) {
                    Text(
                        text = stringResource(id = R.string.text_cancel)
                    )
                }
            },
            onDismissRequest = onClosedDialog
        )
    }
}