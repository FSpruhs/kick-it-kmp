package com.spruhs.auth.data

import com.spruhs.auth.application.AuthenticationRepository

class AuthenticationRepositoryImpl : AuthenticationRepository {
    override suspend fun refreshToken(refreshToken: String): Pair<String, String> {
        TODO("Not yet implemented")
    }
}