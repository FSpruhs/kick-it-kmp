package com.spruhs.auth.application

interface AuthenticationRepository {
    suspend fun refreshToken(refreshToken: String): Pair<String, String>
}