package com.spruhs.user.application

class LoadUserUseCase(private val userRepository: UserRepository) {
    suspend fun loadUser(userId: String) {
        userRepository.loadUser(userId)
    }
}