package com.spruhs.group.presentation

import com.spruhs.BaseUIState
import com.spruhs.BaseViewModel
import com.spruhs.group.application.GroupRepository
import com.spruhs.user.application.UserRepository
import com.spruhs.validateEmail
import kotlinx.coroutines.flow.update

class InvitePlayerViewModel(
    private val groupRepository: GroupRepository,
    private val userRepository: UserRepository
) : BaseViewModel<InvitePlayerIntent, InvitePlayerEffect, InvitePlayerUIState>(
    InvitePlayerUIState()
) {

    override fun processIntent(intent: InvitePlayerIntent) {
        when (intent) {
            is InvitePlayerIntent.InvitePlayer -> handlePlayerInvited()
            is InvitePlayerIntent.EmailChanged -> handleEmailChanged(intent.playerEmail)
        }
    }

    private fun handlePlayerInvited() {
        performAction(
            onSuccess = {
                effectsMutable.emit(InvitePlayerEffect.PlayerInvited)
            },
            onError = {
                effectsMutable.emit(InvitePlayerEffect.ShowError("Inviting player failed"))
            },
            action = {
                val selectedGroup = userRepository.selectedGroup
                groupRepository.invitePlayer(selectedGroup.value!!.id, uiState.value.email)
            }
        )
    }

    private fun handleEmailChanged(email: String) {
        uiStateMutable.update {
            it.copy(
                email = email,
                isEmailValid = validateEmail(email)
            )
        }
    }
}

data class InvitePlayerUIState(
    val email: String = "",
    val isEmailValid: Boolean = false,
    override val isLoading: Boolean = false,
    override val error: String? = null
) : BaseUIState<InvitePlayerUIState> {
    override fun copyWith(isLoading: Boolean, error: String?): InvitePlayerUIState =
        copy(isLoading = isLoading, error = error)
}

sealed class InvitePlayerEffect {
    object PlayerInvited : InvitePlayerEffect()
    data class ShowError(val message: String) : InvitePlayerEffect()
}

sealed class InvitePlayerIntent {
    object InvitePlayer : InvitePlayerIntent()
    data class EmailChanged(val playerEmail: String) : InvitePlayerIntent()
}