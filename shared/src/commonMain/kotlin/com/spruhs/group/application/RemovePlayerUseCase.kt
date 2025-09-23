package com.spruhs.group.application

import com.spruhs.user.application.UserRepository

class RemovePlayerUseCase(
    private val groupRepository: GroupRepository,
    private val userRepository: UserRepository

) {
    suspend fun remove(playerId: String) {
        val selectedGroup = userRepository.getSelectedGroupOrThrow()
        groupRepository.removePlayer(selectedGroup.id, playerId)
    }
}