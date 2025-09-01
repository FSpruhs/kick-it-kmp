package com.spruhs.auth.data

import com.spruhs.auth.application.AuthApi
import com.spruhs.auth.application.AuthTokens

class AuthApiImpl : AuthApi {
    override suspend fun login(email: String, password: String): AuthTokens? {
        TODO("Not yet implemented")
    }

    override suspend fun refreshToken(refreshToken: String): AuthTokens {
        TODO("Not yet implemented")
    }
}