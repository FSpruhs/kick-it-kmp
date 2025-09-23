package com.spruhs.group.application

import com.spruhs.user.application.UserRepository

class LeaveGroupUseCase(
    private val userRepository: UserRepository,
    private val groupRepository: GroupRepository
) {
    suspend fun leave(groupId: String) {
        val user = userRepository.userState.value ?: throw IllegalStateException("No user")
        groupRepository.removePlayer(groupId, user.userId)
        userRepository.removeGroup(groupId)
    }
}