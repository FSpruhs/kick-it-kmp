package com.spruhs

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

fun validateEmail(email: String): Boolean = email.contains("@") &&
    email.contains(".") &&
    email.length > 5

@OptIn(ExperimentalTime::class)
fun dateTimeNow() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())