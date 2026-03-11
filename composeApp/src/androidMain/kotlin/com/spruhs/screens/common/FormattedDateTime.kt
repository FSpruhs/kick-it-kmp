package com.spruhs.screens.common

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import kotlinx.datetime.LocalDateTime

@Composable
fun FormattedDateTime(
    dateTime: LocalDateTime,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = Color.Unspecified
) {
    Text(
        text = dateTime.formatDateTime(),
        modifier = modifier,
        style = style,
        color = color
    )
}

@Composable
fun FormattedDate(
    dateTime: LocalDateTime,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = Color.Unspecified
) {
    Text(
        text = dateTime.formatDate(),
        modifier = modifier,
        style = style,
        color = color
    )
}

fun LocalDateTime.formatDate(): String {
    val dayOfWeek = this.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
        .take(2)
    val day = this.date.day.toString().padStart(2, '0')
    val month = this.date.month.ordinal.plus(1).toString().padStart(2, '0')
    val year = this.year

    return "$dayOfWeek, $day.$month.$year"
}

fun LocalDateTime.formatTime(): String {
    val hour = this.hour.toString().padStart(2, '0')
    val minute = this.minute.toString().padStart(2, '0')

    return "$hour:$minute"
}

fun LocalDateTime.formatDateTime(): String = "${formatDate()} • ${formatTime()}"