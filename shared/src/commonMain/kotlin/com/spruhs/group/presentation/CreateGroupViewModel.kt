package com.spruhs.group.presentation

import androidx.lifecycle.viewModelScope
import com.spruhs.AppLogger
import com.spruhs.BaseUIState
import com.spruhs.BaseViewModel
import com.spruhs.group.application.GroupRepository
import com.spruhs.user.application.UserGroupInfo
import com.spruhs.user.application.UserRepository
import com.spruhs.user.application.UserRole
import com.spruhs.user.application.UserStatus
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateGroupViewModel(
    private val groupRepository: GroupRepository,
    private val userRepository: UserRepository
) :
    BaseViewModel<CreateGroupEffect, CreateGroupUIState>(CreateGroupUIState()) {

    fun processIntent(intent: CreateGroupIntent) {
        when (intent) {
            is CreateGroupIntent.CreateGroup -> handleCreateGroup()
            is CreateGroupIntent.GroupNameChanged -> handleNewGroupNameChanged(intent.groupName)
        }
    }

    private fun handleCreateGroup() {
        uiStateMutable.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val result = groupRepository.createGroup(uiState.value.groupName)
                uiStateMutable.update { it.copy(isLoading = false) }
                effectsMutable.emit(CreateGroupEffect.GroupCreated)
                userRepository.addGroup(UserGroupInfo(
                    id = result,
                    name = uiState.value.groupName,
                    userStatus = UserStatus.ACTIVE,
                    userRole = UserRole.COACH,
                ))
            } catch (e: Exception) {
                AppLogger.e("CreateGroupViewModel", "Error creating group", e)
                uiStateMutable.update { it.copy(isLoading = false) }
                effectsMutable.emit(CreateGroupEffect.ShowError("Error creating group"))
            }
        }
    }

    private fun handleNewGroupNameChanged(groupName: String) {
        if (groupName.length > uiState.value.maxChars) return
        uiStateMutable.update { it.copy(groupName = groupName) }
    }
}

data class CreateGroupUIState(
    val groupName: String = "",
    val maxChars: Int = 20,
    override val isLoading: Boolean = false,
    override val error: String? = null
) : BaseUIState

sealed class CreateGroupEffect {
    object GroupCreated : CreateGroupEffect()
    data class ShowError(val message: String) : CreateGroupEffect()
}

sealed class CreateGroupIntent {
    data class GroupNameChanged(val groupName: String) : CreateGroupIntent()
    object CreateGroup : CreateGroupIntent()
}