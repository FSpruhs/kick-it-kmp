package com.spruhs.auth.application

interface AuthTokenRepository {
    suspend fun getToken(): AuthToken?
    suspend fun saveToken(accessToken: String, refreshToken: String)
    suspend fun deleteToken()
    suspend fun refreshToken(refreshToken: String): Pair<String, String>
}