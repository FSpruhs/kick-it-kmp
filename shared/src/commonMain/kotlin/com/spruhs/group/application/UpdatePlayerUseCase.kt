package com.spruhs.group.application

import com.spruhs.user.application.UserRepository
import com.spruhs.user.application.UserRole
import com.spruhs.user.application.UserStatus

class UpdatePlayerUseCase(
    private val groupRepository: GroupRepository,
    private val userRepository: UserRepository
) {

    suspend fun update(playerId: String, status: UserStatus, role: UserRole) {
        val groupId = userRepository.getSelectedGroupOrThrow().id
        groupRepository.updatePlayer(groupId, playerId, status, role)
    }
}