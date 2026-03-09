package com.spruhs.user.application

class LogoutUseCase(private val userRepository: UserRepository) {
    suspend fun logout() {
        userRepository.logout()
    }
}