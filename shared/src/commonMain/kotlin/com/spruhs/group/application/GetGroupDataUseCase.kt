package com.spruhs.group.application

import com.spruhs.user.application.SelectedGroup
import com.spruhs.user.application.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class GetGroupDataUseCase(
    private val userRepository: UserRepository,
    private val groupRepository: GroupRepository
) {

    suspend fun getResult(): Result {
        val selectedGroup = userRepository.getSelectedGroupOrThrow()

        val (group, groupNames) = coroutineScope {
            val groupDeferred = async { groupRepository.getGroup(selectedGroup.id) }
            val groupNamesDeferred = async { groupRepository.getGroupNames(selectedGroup.id) }

            groupDeferred.await() to groupNamesDeferred.await()
        }

        return Result(
            selectedGroup,
            group,
            groupNames.associate { it.id to it.name }
        )
    }

    data class Result(
        val selectedGroup: SelectedGroup,
        val group: Group,
        val groupNames: Map<String, String>
    )
}
