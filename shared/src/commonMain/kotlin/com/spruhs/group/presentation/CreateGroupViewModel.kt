package com.spruhs.group.presentation

import com.spruhs.BaseViewModel

class CreateGroupViewModel : BaseViewModel<CreateGroupEffect, CreateGroupUIState>(CreateGroupUIState()) {

    fun processIntent(intent: CreateGroupIntent) {
        when (intent) {
            is CreateGroupIntent.CreateGroup -> TODO()
            is CreateGroupIntent.NewGroupNameChanged -> TODO()
        }
    }

}

data class CreateGroupUIState(
    val isLoading: Boolean = false,
    val newGroupName: String = "",
    val maxChars: Int = 20,
    val error: String? = null
)

sealed class CreateGroupEffect {
    object GroupCreated : CreateGroupEffect()
}

sealed class CreateGroupIntent {
    data class NewGroupNameChanged(val newGroupName: String) : CreateGroupIntent()
    object CreateGroup : CreateGroupIntent()

}