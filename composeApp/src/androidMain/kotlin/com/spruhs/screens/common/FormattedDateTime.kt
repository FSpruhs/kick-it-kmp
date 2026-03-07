package com.spruhs.screens.common

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import kotlinx.datetime.LocalDateTime

@Composable
fun FormattedDateTimeInline(
    dateTime: LocalDateTime,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = Color.Unspecified
) {
    Text(
        text = "${formatDate(dateTime)} • ${formatTime(dateTime)}",
        modifier = modifier,
        style = style,
        color = color
    )
}

private fun formatDate(dateTime: LocalDateTime): String {
    val dayOfWeek = dateTime.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
        .take(2)
    val day = dateTime.date.day.toString().padStart(2, '0')
    val month = dateTime.date.month.ordinal.plus(1).toString().padStart(2, '0')
    val year = dateTime.year

    return "$dayOfWeek, $day.$month.$year"
}

private fun formatTime(dateTime: LocalDateTime): String {
    val hour = dateTime.hour.toString().padStart(2, '0')
    val minute = dateTime.minute.toString().padStart(2, '0')

    return "$hour:$minute"
}

