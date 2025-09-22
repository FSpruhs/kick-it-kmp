package com.spruhs

fun validateEmail(email: String): Boolean = email.contains("@") &&
    email.contains(".") &&
    email.length > 5