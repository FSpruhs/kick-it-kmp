package com.spruhs.group.presentation

import com.spruhs.BaseUIState
import com.spruhs.BaseViewModel

class InvitePlayerViewModel :
    BaseViewModel<InvitePlayerEffect, InvitePlayerUIState>(InvitePlayerUIState()) {

    fun processIntent(intent: InvitePlayerIntent) {
        when (intent) {
            is InvitePlayerIntent.InvitePlayer -> TODO()
            is InvitePlayerIntent.PlayerEmailChanged -> TODO()
        }
    }
}

data class InvitePlayerUIState(
    val playerEmail: String = "",
    override val isLoading: Boolean = false
) : BaseUIState<InvitePlayerUIState> {
    override fun copyWith(isLoading: Boolean): InvitePlayerUIState = copy(isLoading = isLoading)
}

sealed class InvitePlayerEffect {
    object PlayerInvited : InvitePlayerEffect()
}

sealed class InvitePlayerIntent {
    object InvitePlayer : InvitePlayerIntent()
    data class PlayerEmailChanged(val playerEmail: String) : InvitePlayerIntent()
}