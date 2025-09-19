package com.spruhs.group.presentation

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
    val isLoading: Boolean = false,
    val playerEmail: String = "",
    val error: String? = null
)

sealed class InvitePlayerEffect {
    object PlayerInvited : InvitePlayerEffect()
}

sealed class InvitePlayerIntent {
    object InvitePlayer : InvitePlayerIntent()
    data class PlayerEmailChanged(val playerEmail: String) : InvitePlayerIntent()
}