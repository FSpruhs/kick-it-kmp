package com.spruhs.user.data

import com.spruhs.user.application.User

class UserApi {
    suspend fun getUser(id: String): User {
        TODO("Not yet implemented")
    }

    suspend fun registerUser(nickName: String, email: String, password: String): String {
        TODO("Not yet implemented")
    }

    suspend fun changeNickName(userId: String, nickname: String) {
        TODO("Not yet implemented")
    }
}