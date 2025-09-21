package com.spruhs.user.presentation

import com.spruhs.BaseUIState
import com.spruhs.BaseViewModel
import com.spruhs.user.application.UserGroupInfo

class SelectGroupViewModel :
    BaseViewModel<SelectGroupEffect, SelectGroupUIState>(SelectGroupUIState()) {
    fun processIntent(intent: SelectGroupIntent) {
        when (intent) {
            is SelectGroupIntent.CreateGroup -> {}
            is SelectGroupIntent.SelectGroup -> {}
        }
    }
}

data class SelectGroupUIState(
    override val isLoading: Boolean = false,
    override val error: String? = null,
    val id: String? = null,
    val groups: List<UserGroupInfo> = emptyList()
) : BaseUIState<SelectGroupUIState> {
    override fun copyWith(isLoading: Boolean): SelectGroupUIState = copy(isLoading = isLoading)
}

sealed class SelectGroupEffect {
    object GroupSelected : SelectGroupEffect()
}

sealed class SelectGroupIntent {
    data class SelectGroup(val id: String) : SelectGroupIntent()
    object CreateGroup : SelectGroupIntent()
}