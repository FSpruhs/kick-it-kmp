package com.spruhs.user.application

import kotlinx.coroutines.flow.firstOrNull

class LoadUserUseCase(private val userRepository: UserRepository) {
    suspend fun loadUser(userId: String) {
        userRepository.loadUser(userId)
    }

    suspend fun getUser(): User? = userRepository.userState.firstOrNull()

    suspend fun getSelectedGroup(): SelectedGroup? = userRepository.selectedGroup.firstOrNull()
}