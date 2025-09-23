package com.spruhs.group.presentation

import androidx.lifecycle.viewModelScope
import com.spruhs.BaseUIState
import com.spruhs.BaseViewModel
import com.spruhs.group.application.GetGroupDataUseCase
import com.spruhs.group.application.PlayerDetails
import com.spruhs.group.application.RemoveGroupUseCase
import com.spruhs.user.application.SelectedGroup
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GroupViewModel(
    private val getGroupDataUseCase: GetGroupDataUseCase,
    private val leaveGroupUseCase: RemoveGroupUseCase
) : BaseViewModel<GroupIntent, GroupEffect, GroupUiState>(GroupUiState()) {

    init {
        performAction(
            onSuccess = {
                uiStateMutable.update {
                    it.copy(
                        selectedGroup = it.selectedGroup,
                        players = it.players,
                        groupNames = it.groupNames
                    )
                }
            },
            action = getGroupDataUseCase::getResult
        )
    }

    override fun processIntent(intent: GroupIntent) {
        when (intent) {
            is GroupIntent.LeaveGroup -> handleLeaveGroup()
            is GroupIntent.SelectPlayer -> handleSelectPlayer(intent.playerId)
        }
    }

    private fun handleLeaveGroup() {
        performAction(
            onSuccess = { effectsMutable.emit(GroupEffect.LeavedGroup) },
            onError = {
                effectsMutable.emit(GroupEffect.ShowError("Failed to leave group"))
            },
            action = { leaveGroupUseCase.leave(uiState.value.selectedGroup!!.id) }
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