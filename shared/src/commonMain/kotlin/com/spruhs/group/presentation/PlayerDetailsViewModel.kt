package com.spruhs.group.presentation

import com.spruhs.BaseUIState
import com.spruhs.BaseViewModel
import com.spruhs.group.application.PlayerDetails
import com.spruhs.group.application.RemovePlayerUseCase
import com.spruhs.match.application.Match
import com.spruhs.statistics.application.PlayerStats
import com.spruhs.user.application.SelectedGroup
import com.spruhs.user.application.UserRole
import com.spruhs.user.application.UserStatus

class PlayerDetailsViewModel(
    private val playerId: String,
    private val removePlayerUseCase: RemovePlayerUseCase
) : BaseViewModel<PlayerDetailsIntent, PlayerDetailsEffect, PlayerDetailsUIState>(
    PlayerDetailsUIState()
) {
    override fun processIntent(intent: PlayerDetailsIntent) {
        when (intent) {
            is PlayerDetailsIntent.RemovePlayer -> handleRemovePlayer()
            is PlayerDetailsIntent.UpdatePlayer -> {}
            is PlayerDetailsIntent.SelectRole -> {}
            is PlayerDetailsIntent.SelectStatus -> {}
            is PlayerDetailsIntent.SelectLastMatch -> {}
        }
    }

    private fun handleRemovePlayer() = performAction(
        action = { removePlayerUseCase.remove(playerId) },
        onSuccess = { effectsMutable.emit(PlayerDetailsEffect.PlayerRemoved) }
    )
}

data class PlayerDetailsUIState(
    override val isLoading: Boolean = false,
    override val error: String? = null,
    val playerDetails: PlayerDetails? = null,
    val groupNames: Map<String, String> = emptyMap(),
    val playerStats: PlayerStats = PlayerStats(),
    val lastMatches: List<Match> = emptyList(),
    val selectedGroup: SelectedGroup? = null,
    val selectedStatus: UserStatus? = null,
    val selectedRole: UserRole? = null
) : BaseUIState<PlayerDetailsUIState> {
    override fun copyWith(isLoading: Boolean, error: String?): PlayerDetailsUIState =
        copy(isLoading = isLoading, error = error)
}

sealed class PlayerDetailsEffect {
    object PlayerRemoved : PlayerDetailsEffect()
    object PlayerUpdated : PlayerDetailsEffect()
    data class MatchSelected(val matchId: String) : PlayerDetailsEffect()
    data class ShowError(val message: String) : PlayerDetailsEffect()
}

sealed class PlayerDetailsIntent {
    object RemovePlayer : PlayerDetailsIntent()
    object UpdatePlayer : PlayerDetailsIntent()
    data class SelectStatus(val status: String) : PlayerDetailsIntent()
    data class SelectRole(val role: String) : PlayerDetailsIntent()
    data class SelectLastMatch(val matchId: String) : PlayerDetailsIntent()
}