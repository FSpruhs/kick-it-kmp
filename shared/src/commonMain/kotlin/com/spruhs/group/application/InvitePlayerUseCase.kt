package com.spruhs.group.application

import com.spruhs.user.application.UserRepository

class InvitePlayerUseCase(
    private val groupRepository: GroupRepository,
    private val userRepository: UserRepository,
) {
    suspend fun invite(email: String) {
        val selectedGroup = userRepository.getSelectedGroupOrThrow()
        groupRepository.invitePlayer(selectedGroup.id, email)
    }
}