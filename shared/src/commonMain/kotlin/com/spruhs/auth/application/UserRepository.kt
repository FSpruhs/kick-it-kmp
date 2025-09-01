package com.spruhs.auth.application

interface UserRepository {
    suspend fun loadUser(userId: String)
}