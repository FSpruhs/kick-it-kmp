package com.spruhs.auth.application

interface AuthApi {
    suspend fun login(email: String, password: String): Pair<String?, String?>
    suspend fun refreshToken(refreshToken: String): Pair<String, String>
}