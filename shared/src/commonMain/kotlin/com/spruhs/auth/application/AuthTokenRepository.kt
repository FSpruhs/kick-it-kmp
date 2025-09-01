package com.spruhs.auth.application

interface AuthTokenRepository {
    suspend fun getToken(): AuthTokens?
    suspend fun saveToken(authTokens: AuthTokens)
    suspend fun deleteToken()
    suspend fun refreshToken(refreshToken: String): AuthTokens
}