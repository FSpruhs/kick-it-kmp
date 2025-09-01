package com.spruhs.auth.application

interface AuthApi {
    suspend fun login(email: String, password: String): AuthTokens?
    suspend fun refreshToken(refreshToken: String): AuthTokens
}