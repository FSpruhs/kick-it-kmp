package com.spruhs.user.presentation

import androidx.lifecycle.viewModelScope
import com.spruhs.AppLogger
import com.spruhs.BaseUIState
import com.spruhs.BaseViewModel
import com.spruhs.user.application.LoadUserUseCase
import com.spruhs.user.application.SelectGroupUseCase
import com.spruhs.user.application.UserGroupInfo
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SelectGroupViewModel(
    private val loadUserUseCase: LoadUserUseCase,
    private val selectGroupUseCase: SelectGroupUseCase
) : BaseViewModel<SelectGroupIntent, SelectGroupEffect, SelectGroupUIState>(SelectGroupUIState()) {

    init {
        loadState()
    }

    private fun loadState() {
        viewModelScope.launch {
            val user = loadUserUseCase.getUser()
            val selectedGroup = loadUserUseCase.getSelectedGroup()
            if (user != null) {
                uiStateMutable.update { state ->
                    state.copy(
                        id = selectedGroup?.id,
                        groups = user.groups.values.toList()
                    )
                }
            } else {
                AppLogger.e("SelectGroupViewModel", "User not found in state")
            }
        }
    }

    override fun processIntent(intent: SelectGroupIntent) {
        when (intent) {
            is SelectGroupIntent.CreateGroup -> viewModelScope.launch {
                effectsMutable.emit(SelectGroupEffect.OnCreateGroupClicked)
            }
            is SelectGroupIntent.SelectGroup -> viewModelScope.launch {
                handleSelectGroup(intent)
            }
        }
    }

    private suspend fun handleSelectGroup(intent: SelectGroupIntent.SelectGroup) {
        val group = uiState.value.groups.firstOrNull { it.id == intent.id }
        if (group != null) {
            selectGroupUseCase.selectGroup(group)
            uiStateMutable.update { it.copy(id = intent.id) }
            effectsMutable.emit(SelectGroupEffect.GroupSelected(intent.id))
        } else {
            AppLogger.e(
                "SelectGroupViewModel",
                "Group with id ${intent.id} not found in state"
            )
        }
    }
}

data class SelectGroupUIState(
    override val isLoading: Boolean = false,
    override val error: String? = null,
    val id: String? = null,
    val groups: List<UserGroupInfo> = emptyList()
) : BaseUIState<SelectGroupUIState> {
    override fun copyWith(isLoading: Boolean, error: String?): SelectGroupUIState =
        copy(isLoading = isLoading, error = error)
}

sealed class SelectGroupEffect {
    data class GroupSelected(val id: String) : SelectGroupEffect()
    object OnCreateGroupClicked : SelectGroupEffect()
}

sealed class SelectGroupIntent {
    data class SelectGroup(val id: String) : SelectGroupIntent()
    object CreateGroup : SelectGroupIntent()
}