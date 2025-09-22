package com.spruhs.group.presentation

import androidx.lifecycle.viewModelScope
import com.spruhs.BaseUIState
import com.spruhs.BaseViewModel
import com.spruhs.group.application.GroupRepository
import com.spruhs.group.application.PlayerDetails
import com.spruhs.user.application.SelectedGroup
import com.spruhs.user.application.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GroupViewModel(
    private val userRepository: UserRepository,
    private val groupRepository: GroupRepository
) : BaseViewModel<GroupEffect, GroupUiState>(GroupUiState()) {

    init {
        fetchData {
            userRepository.selectedGroup.value?.let { selectedGroup ->
                fetchGroupData(selectedGroup)
            }
        }
    }

    private suspend fun fetchGroupData(selectedGroup: SelectedGroup) = coroutineScope {
        val groupDeferred = async { groupRepository.getGroup(selectedGroup.id) }
        val groupNamesDeferred = async { groupRepository.getGroupNames(selectedGroup.id) }

        val group = groupDeferred.await()
        val groupNames = groupNamesDeferred.await()
        uiStateMutable.update {
            it.copy(
                selectedGroup = selectedGroup,
                players = group.players,
                groupNames = groupNames.associate { entry -> entry.id to entry.name }
            )
        }
    }

    fun processIntent(intent: GroupIntent) {
        when (intent) {
            is GroupIntent.LeaveGroup -> handleLeaveGroup()
            is GroupIntent.SelectPlayer -> handleSelectPlayer(intent.playerId)
        }
    }

    private fun handleLeaveGroup() {
        performAction(
            onSuccess = {
                userRepository.removeGroup(uiState.value.selectedGroup!!.id)
                effectsMutable.emit(GroupEffect.LeavedGroup)
            },
            onError = {
                effectsMutable.emit(GroupEffect.ShowError("Failed to leave group"))
            },
            action = {
                val user = userRepository.userState.value
                groupRepository.removePlayer(uiState.value.selectedGroup!!.id, user!!.userId)
            }
        )
    }

    private fun handleSelectPlayer(playerId: String) {
        viewModelScope.launch {
            effectsMutable.emit(GroupEffect.PlayerSelected(playerId))
        }
    }
}

data class GroupUiState(
    override val isLoading: Boolean = false,
    override val error: String? = null,
    val selectedGroup: SelectedGroup? = null,
    val players: List<PlayerDetails> = emptyList(),
    val groupNames: Map<String, String> = emptyMap()
) : BaseUIState<GroupUiState> {
    override fun copyWith(isLoading: Boolean, error: String?): GroupUiState =
        copy(isLoading = isLoading, error = error)
}

sealed class GroupEffect {
    object LeavedGroup : GroupEffect()
    data class PlayerSelected(val playerId: String) : GroupEffect()
    data class ShowError(val message: String) : GroupEffect()
}

sealed class GroupIntent {
    object LeaveGroup : GroupIntent()
    data class SelectPlayer(val playerId: String) : GroupIntent()
}