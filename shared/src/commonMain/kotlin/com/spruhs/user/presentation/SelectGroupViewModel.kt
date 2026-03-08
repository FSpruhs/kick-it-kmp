package com.spruhs.user.presentation

import androidx.lifecycle.viewModelScope
import com.spruhs.AppLogger
import com.spruhs.BaseUIState
import com.spruhs.BaseViewModel
import com.spruhs.user.application.UserGroupInfo
import com.spruhs.user.application.UserRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SelectGroupViewModel(private val userRepository: UserRepository) :
    BaseViewModel<SelectGroupIntent, SelectGroupEffect, SelectGroupUIState>(SelectGroupUIState()) {

    init {
        loadState()
    }

    private fun loadState() {
        viewModelScope.launch {
            val user = userRepository.userState.firstOrNull()
            val selectedGroup = userRepository.selectedGroup.firstOrNull()
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
                val group = uiState.value.groups.firstOrNull { it.id == intent.id }
                if (group != null) {
                    userRepository.setSelectedGroup(group)
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