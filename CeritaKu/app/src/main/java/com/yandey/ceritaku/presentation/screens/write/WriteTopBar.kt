package com.yandey.ceritaku.presentation.screens.write

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockConfig
import com.maxkeppeler.sheets.clock.models.ClockSelection
import com.yandey.ceritaku.model.Story
import com.yandey.ceritaku.presentation.components.DisplayAlertDialog
import com.yandey.ceritaku.util.Constants.DATE_FORMAT
import com.yandey.ceritaku.util.Constants.DATE_TIME_FORMAT
import com.yandey.ceritaku.util.Constants.TIME_FORMAT
import com.yandey.ceritaku.util.calculateTimeUntilNextMinute
import com.yandey.ceritaku.util.toInstant
import com.yandey.deardiary.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteTopBar(
    selectedStory: Story?,
    moodName: () -> String,
    onBackPressed: () -> Unit,
    onDeleteConfirmed: () -> Unit,
    onDateTimeUpdated: (ZonedDateTime) -> Unit,
) {
    var dateTimeUpdated by remember { mutableStateOf(false) }
    var realTimeUpdated by remember { mutableStateOf(true) }
    var confirmedDateDialog by remember { mutableStateOf(false) }
    var confirmedTimeDialog by remember { mutableStateOf(false) }

    val dateDialog = rememberUseCaseState(
        onDismissRequest = { realTimeUpdated = true },
        onFinishedRequest = { realTimeUpdated = !confirmedDateDialog }
    )
    val timeDialog = rememberUseCaseState(
        onDismissRequest = { realTimeUpdated = true },
        onFinishedRequest = { realTimeUpdated = !confirmedTimeDialog }
    )
    var currentDate by remember { mutableStateOf(LocalDate.now()) }
    var currentTime by remember { mutableStateOf(LocalTime.now()) }

    val scope = rememberCoroutineScope()
    LaunchedEffect(realTimeUpdated) {
        scope.launch {
            while (realTimeUpdated) {
                if (!dateTimeUpdated) {
                    val timeUntilNextMinute = calculateTimeUntilNextMinute()
                    currentDate = LocalDate.now()
                    currentTime = LocalTime.now()
                    delay(timeUntilNextMinute)
                }
            }
        }
    }

    val formattedDate = remember(key1 = currentDate) {
        DateTimeFormatter
            .ofPattern(DATE_FORMAT)
            .format(currentDate).uppercase()
    }
    val formattedTime = remember(key1 = currentTime) {
        DateTimeFormatter
            .ofPattern(TIME_FORMAT)
            .format(currentTime)
    }
    val selectedStoryDateTime = remember(currentDate, currentTime, selectedStory) {
        if (selectedStory != null && dateTimeUpdated) {
            "$formattedDate, $formattedTime"
        } else if (selectedStory != null) {
            SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
                .format(Date.from(selectedStory.date.toInstant())).uppercase()
        } else {
            "$formattedDate, $formattedTime"
        }
    }

    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Arrow Back Icon",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        title = {
            Column {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = moodName(),
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = selectedStoryDateTime,
                    style = TextStyle(fontSize = MaterialTheme.typography.bodySmall.fontSize),
                    textAlign = TextAlign.Center
                )
            }
        },
        actions = {
            if (dateTimeUpdated) {
                IconButton(onClick = {
                    currentDate = LocalDate.now()
                    currentTime = LocalTime.now()
                    dateTimeUpdated = false
                    realTimeUpdated = true
                }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Icon",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            } else {
                IconButton(onClick = {
                    dateDialog.show()
                    realTimeUpdated = false
                }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Date Range Icon",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            if (selectedStory != null) {
                DeleteStoryAction(
                    selectedStory = selectedStory,
                    onDeleteConfirmed = onDeleteConfirmed
                )
            }
        }
    )

    CalendarDialog(
        state = dateDialog,
        selection = CalendarSelection.Date { localDate ->
            currentDate = localDate
            confirmedDateDialog = true
            timeDialog.show()
        },
        config = CalendarConfig(yearSelection = true, monthSelection = true)
    )

    ClockDialog(
        state = timeDialog,
        selection = ClockSelection.HoursMinutes { hours, minutes ->
            dateTimeUpdated = true
            confirmedTimeDialog = true
            currentTime = LocalTime.of(hours, minutes, 0)
            onDateTimeUpdated(
                ZonedDateTime.of(
                    currentDate,
                    currentTime,
                    ZoneId.systemDefault()
                )
            )
        },
        config = ClockConfig(
            defaultTime = currentTime,
            is24HourFormat = true
        )
    )
}

@Composable
fun DeleteStoryAction(
    selectedStory: Story?,
    onDeleteConfirmed: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var openDialog by remember { mutableStateOf(false) }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        DropdownMenuItem(
            text = {
                Text(text = stringResource(id = R.string.drop_down_delete))
            },
            onClick = {
                openDialog = true
                expanded = false
            }
        )
    }
    DisplayAlertDialog(
        titles = stringResource(id = R.string.title_alert_dialog_delete_story),
        message = stringResource(
            id = R.string.message_alert_dialog_delete_story,
            "${selectedStory?.title}"
        ),
        isDialogOpened = openDialog,
        onClosedDialog = { openDialog = false },
        onYesClicked = onDeleteConfirmed
    )
    IconButton(onClick = { expanded = !expanded }) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "More Vert Icon",
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}