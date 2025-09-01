package com.spruhs.user.application

interface UserApi {
    suspend fun getUser(id: String): User
    suspend fun registerUser(nickName: String, email: String, password: String): String
    suspend fun changeNickName(userId: String, nickname: String)
}