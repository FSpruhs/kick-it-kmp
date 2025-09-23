package com.spruhs.auth.application

import com.spruhs.user.application.UserRepository

class RegisterUseCase(private val userRepository: UserRepository) {

    suspend fun register(email: String, password: String, nickName: String) {
        userRepository.register(email, password, nickName)
    }
}