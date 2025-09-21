package com.spruhs.group.presentation

import com.spruhs.BaseUIState
import com.spruhs.BaseViewModel
import com.spruhs.group.application.PlayerDetails
import com.spruhs.user.application.SelectedGroup

class GroupViewModel : BaseViewModel<GroupEffect, GroupUiState>(GroupUiState()) {
    fun processIntent(intent: GroupIntent) {
        when (intent) {
            is GroupIntent.LeaveGroup -> {}
            is GroupIntent.SelectPlayer -> {}
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
}

sealed class GroupIntent {
    object LeaveGroup : GroupIntent()
    data class SelectPlayer(val playerId: String) : GroupIntent()
}