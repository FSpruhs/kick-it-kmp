package com.spruhs.user.application

class ChangeNicknameUseCase(private val userRepository: UserRepository) {
    suspend fun change(newNickname: String) {
        userRepository.changeNickname(newNickname)
    }
}