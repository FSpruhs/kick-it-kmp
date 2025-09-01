package com.spruhs.auth.application

interface TokenRepository {
    suspend fun getToken(): AuthToken?
    suspend fun saveToken(accessToken: String, refreshToken: String)
}