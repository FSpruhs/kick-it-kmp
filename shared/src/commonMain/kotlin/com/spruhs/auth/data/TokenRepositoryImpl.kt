package com.spruhs.auth.data

import com.spruhs.auth.application.AuthToken
import com.spruhs.auth.application.TokenRepository

class TokenRepositoryImpl : TokenRepository {
    override suspend fun getToken(): AuthToken? {
        TODO("Not yet implemented")
    }

    override suspend fun saveToken(accessToken: String, refreshToken: String) {
        TODO("Not yet implemented")
    }
}