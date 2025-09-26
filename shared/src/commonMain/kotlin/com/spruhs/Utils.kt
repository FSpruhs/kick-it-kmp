package com.spruhs

import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun validateEmail(email: String): Boolean = email.contains("@") &&
    email.contains(".") &&
    email.length > 5

@OptIn(ExperimentalTime::class)
fun dateTimeNow() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())