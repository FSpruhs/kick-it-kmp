package com.spruhs.user.application

class SelectGroupUseCase(private val userRepository: UserRepository) {
    suspend fun selectGroup(group: UserGroupInfo) {
        userRepository.setSelectedGroup(group)
    }
}