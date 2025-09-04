package com.spruhs.main

import androidx.lifecycle.viewModelScope
import com.spruhs.BaseViewModel
import com.spruhs.user.application.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TopBarViewModel(userRepository: UserRepository) :
    BaseViewModel<TopBarEffect, TopBarUIState>(TopBarUIState()) {

    override val uiState: StateFlow<TopBarUIState> = combine(
        userRepository.userState,
        userRepository.selectedGroup
    ) { user, group ->
        TopBarUIState(
            selectedGroupName = group?.name,
            unreadMessage = 0,
            imageUrl = user?.imageUrl
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TopBarUIState()
    )

    fun processIntent(intent: TopBarIntent) {
        when (intent) {
            TopBarIntent.Back -> viewModelScope.launch { effectsMutable.emit(TopBarEffect.Back) }
            TopBarIntent.Messages -> viewModelScope.launch {
                effectsMutable.emit(TopBarEffect.Messages)
            }
            TopBarIntent.Profile -> viewModelScope.launch {
                effectsMutable.emit(TopBarEffect.Profile)
            }
        }
    }
}

sealed class TopBarIntent {
    object Back : TopBarIntent()
    object Profile : TopBarIntent()
    object Messages : TopBarIntent()
}

sealed class TopBarEffect {
    object Back : TopBarEffect()
    object Profile : TopBarEffect()
    object Messages : TopBarEffect()
}

data class TopBarUIState(
    val selectedGroupName: String? = null,
    val unreadMessage: Int = 0,
    val imageUrl: String? = null
)