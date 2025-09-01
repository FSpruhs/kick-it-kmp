package com.spruhs.auth.data

import com.spruhs.auth.application.UserRepository

class UserRepositoryImpl : UserRepository {
    override suspend fun loadUser(userId: String) {
        TODO("Not yet implemented")
    }
}