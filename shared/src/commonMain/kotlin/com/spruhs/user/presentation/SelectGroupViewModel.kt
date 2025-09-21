package com.spruhs.user.presentation

import com.spruhs.BaseViewModel
import com.spruhs.user.application.UserGroupInfo

class SelectGroupViewModel : BaseViewModel<SelectGroupEffect, SelectGroupUIState>(SelectGroupUIState()) {
    fun processIntent(intent: SelectGroupIntent) {
        when (intent) {
            SelectGroupIntent.SelectGroup -> {}
        }
    }
}

data class SelectGroupUIState(
    val isLoading: Boolean = false,
    val id: String? = null,
    val groups: List<UserGroupInfo> = emptyList()
)

sealed class SelectGroupEffect {
    object GroupSelected : SelectGroupEffect()
}

sealed class SelectGroupIntent {
    data class SelectGroup(val id: String) : SelectGroupIntent()
    object CreateGroup : SelectGroupIntent()
}