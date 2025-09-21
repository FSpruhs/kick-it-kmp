package com.spruhs.group.presentation

import com.spruhs.BaseUIState
import com.spruhs.BaseViewModel

class CreateGroupViewModel :
    BaseViewModel<CreateGroupEffect, CreateGroupUIState>(CreateGroupUIState()) {

    fun processIntent(intent: CreateGroupIntent) {
        when (intent) {
            is CreateGroupIntent.CreateGroup -> TODO()
            is CreateGroupIntent.NewGroupNameChanged -> TODO()
        }
    }
}

data class CreateGroupUIState(
    val newGroupName: String = "",
    val maxChars: Int = 20,
    override val isLoading: Boolean = false,
    override val error: String? = null
) : BaseUIState

sealed class CreateGroupEffect {
    object GroupCreated : CreateGroupEffect()
}

sealed class CreateGroupIntent {
    data class NewGroupNameChanged(val newGroupName: String) : CreateGroupIntent()
    object CreateGroup : CreateGroupIntent()
}