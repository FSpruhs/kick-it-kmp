package com.spruhs.auth.application

expect class TokenHelper() {

    fun getUserId(token: String): String
    fun isTokenValid(token: String): Boolean
}