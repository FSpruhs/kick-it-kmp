package com.spruhs.auth.data

import com.spruhs.auth.application.AuthApi

class AuthApiImpl : AuthApi {
    override suspend fun login(email: String, password: String): Pair<String?, String?> {
        TODO("Not yet implemented")
    }

    override suspend fun refreshToken(refreshToken: String): Pair<String, String> {
        TODO("Not yet implemented")
    }
}