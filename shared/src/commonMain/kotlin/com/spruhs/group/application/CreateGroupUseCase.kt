package com.spruhs.group.application

import com.spruhs.user.application.UserGroupInfo
import com.spruhs.user.application.UserRepository
import com.spruhs.user.application.UserRole
import com.spruhs.user.application.UserStatus

class CreateGroupUseCase(
    private val groupRepository: GroupRepository,
    private val userRepository: UserRepository,
) {
    suspend fun create(groupName: String) {
        groupRepository.createGroup(groupName).also {
            addUserGroup(it, groupName)
        }
    }

    private fun addUserGroup(id: String, name: String) {
        userRepository.addGroup(
            UserGroupInfo(
                id = id,
                name = name,
                userStatus = UserStatus.ACTIVE,
                userRole = UserRole.COACH
            )
        )
    }
}
