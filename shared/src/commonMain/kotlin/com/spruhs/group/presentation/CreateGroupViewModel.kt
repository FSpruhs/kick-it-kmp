package com.spruhs.group.presentation

import com.spruhs.BaseUIState
import com.spruhs.BaseViewModel
import com.spruhs.group.application.CreateGroupUseCase
import kotlinx.coroutines.flow.update

class CreateGroupViewModel(
    private val createGroupUseCase: CreateGroupUseCase
) : BaseViewModel<CreateGroupIntent, CreateGroupEffect, CreateGroupUIState>(CreateGroupUIState()) {

    override fun processIntent(intent: CreateGroupIntent) {
        when (intent) {
            is CreateGroupIntent.CreateGroup -> handleCreateGroup()
            is CreateGroupIntent.GroupNameChanged -> handleNewGroupNameChanged(intent.groupName)
        }
    }

    private fun handleCreateGroup() {
        performAction(
            onSuccess = { result ->
                effectsMutable.emit(CreateGroupEffect.GroupCreated)
            },
            onError = { effectsMutable.emit(CreateGroupEffect.ShowError("Error creating group")) },
            action = { createGroupUseCase.create(uiState.value.groupName) }
        )
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
) : BaseUIState<CreateGroupUIState> {
    override fun copyWith(isLoading: Boolean, error: String?): CreateGroupUIState =
        copy(isLoading = isLoading, error = error)
}

sealed class CreateGroupEffect {
    object GroupCreated : CreateGroupEffect()
    data class ShowError(val message: String) : CreateGroupEffect()
}

sealed class CreateGroupIntent {
    data class GroupNameChanged(val groupName: String) : CreateGroupIntent()
    object CreateGroup : CreateGroupIntent()
}