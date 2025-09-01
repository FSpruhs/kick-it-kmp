package com.spruhs.user.data

import com.spruhs.user.application.User
import com.spruhs.user.application.UserApi

class UserApiImpl : UserApi {
    override suspend fun getUser(id: String): User {
        TODO("Not yet implemented")
    }

    override suspend fun registerUser(nickName: String, email: String, password: String): String {
        TODO("Not yet implemented")
    }

    override suspend fun changeNickName(userId: String, nickname: String) {
        TODO("Not yet implemented")
    }
}