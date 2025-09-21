package com.spruhs.group.presentation

import com.spruhs.BaseUIState
import com.spruhs.BaseViewModel
import com.spruhs.group.application.GroupRepository
import com.spruhs.user.application.UserGroupInfo
import com.spruhs.user.application.UserRepository
import com.spruhs.user.application.UserRole
import com.spruhs.user.application.UserStatus
import kotlinx.coroutines.flow.update

class CreateGroupViewModel(
    private val groupRepository: GroupRepository,
    private val userRepository: UserRepository
) : BaseViewModel<CreateGroupEffect, CreateGroupUIState>(CreateGroupUIState()) {

    fun processIntent(intent: CreateGroupIntent) {
        when (intent) {
            is CreateGroupIntent.CreateGroup -> handleCreateGroup()
            is CreateGroupIntent.GroupNameChanged -> handleNewGroupNameChanged(intent.groupName)
        }
    }

    private fun handleCreateGroup() {
        performAction(
            onSuccess = { result ->
                effectsMutable.emit(CreateGroupEffect.GroupCreated)
                userRepository.addGroup(toUserGroupInfo(result))
            },
            onError = { effectsMutable.emit(CreateGroupEffect.ShowError("Error creating group")) },
            action = { groupRepository.createGroup(uiState.value.groupName) }
        )
    }

    private fun toUserGroupInfo(id: String) = UserGroupInfo(
        id = id,
        name = uiState.value.groupName,
        userStatus = UserStatus.ACTIVE,
        userRole = UserRole.COACH
    )

    private fun handleNewGroupNameChanged(groupName: String) {
        if (groupName.length > uiState.value.maxChars) return
        uiStateMutable.update { it.copy(groupName = groupName) }
    }
}

data class CreateGroupUIState(
    val groupName: String = "",
    val maxChars: Int = 20,
    override val isLoading: Boolean = false
) : BaseUIState<CreateGroupUIState> {
    override fun copyWith(isLoading: Boolean): CreateGroupUIState = copy(isLoading = isLoading)
}

sealed class CreateGroupEffect {
    object GroupCreated : CreateGroupEffect()
    data class ShowError(val message: String) : CreateGroupEffect()
}

sealed class CreateGroupIntent {
    data class GroupNameChanged(val groupName: String) : CreateGroupIntent()
    object CreateGroup : CreateGroupIntent()
}