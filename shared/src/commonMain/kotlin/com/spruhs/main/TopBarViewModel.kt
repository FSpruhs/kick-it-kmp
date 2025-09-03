package com.spruhs.main

import androidx.lifecycle.viewModelScope
import com.spruhs.BaseViewModel
import kotlinx.coroutines.launch

class TopBarViewModel : BaseViewModel<TopBarEffect, TopBarUIState>(TopBarUIState()) {

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