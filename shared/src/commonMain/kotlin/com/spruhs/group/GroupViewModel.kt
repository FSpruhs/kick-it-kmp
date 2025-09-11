package com.spruhs.group

import com.spruhs.BaseViewModel

class GroupViewModel : BaseViewModel<GroupEffect, GroupUiState>(GroupUiState()) {
    fun processIntent(intent: GroupIntent) {
        when (intent) {
            is GroupIntent.LeaveGroup -> {}
        }
    }
}

data class GroupUiState(val isLoading: Boolean = false)

sealed class GroupEffect {
    object LeavedGroup : GroupEffect()
}

sealed class GroupIntent {
    object LeaveGroup : GroupIntent()
}