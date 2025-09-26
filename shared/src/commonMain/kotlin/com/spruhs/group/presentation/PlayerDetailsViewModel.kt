package com.spruhs.group.presentation

import androidx.lifecycle.viewModelScope
import com.spruhs.BaseUIState
import com.spruhs.BaseViewModel
import com.spruhs.group.application.GetPlayerDetailsUseCase
import com.spruhs.group.application.PlayerDetails
import com.spruhs.group.application.RemovePlayerUseCase
import com.spruhs.group.application.UpdatePlayerUseCase
import com.spruhs.match.application.Match
import com.spruhs.match.application.PlayerMatchPreview
import com.spruhs.match.application.PlayerMatchResult
import com.spruhs.match.application.toLastPreview
import com.spruhs.statistics.application.PlayerStats
import com.spruhs.user.application.SelectedGroup
import com.spruhs.user.application.UserRole
import com.spruhs.user.application.UserStatus
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlayerDetailsViewModel(
    private val playerId: String,
    private val removePlayerUseCase: RemovePlayerUseCase,
    private val updatePlayerUseCase: UpdatePlayerUseCase,
    private val getPlayerDetailsUseCase: GetPlayerDetailsUseCase
) : BaseViewModel<PlayerDetailsIntent, PlayerDetailsEffect, PlayerDetailsUIState>(
    PlayerDetailsUIState()
) {

    init {
        performAction(
            action = { getPlayerDetailsUseCase.getPlayerDetails(playerId) },
            onSuccess = { result ->
                uiStateMutable.update {
                    it.copy(
                        playerDetails = result.player,
                        groupNames = result.groupNames,
                        playerStats = result.statistics,
                        lastMatches = result.lastMatches.map { match ->
                            match.toLastPreview(playerId)
                        },
                        selectedGroup = result.selectedGroup
                    )
                }
            }
        )
    }

    override fun processIntent(intent: PlayerDetailsIntent) {
        when (intent) {
            is PlayerDetailsIntent.RemovePlayer -> handleRemovePlayer()
            is PlayerDetailsIntent.UpdatePlayer -> handleUpdatePlayer()
            is PlayerDetailsIntent.SelectRole -> handleSelectRole(intent.role)
            is PlayerDetailsIntent.SelectStatus -> handleSelectStatus(intent.status)
            is PlayerDetailsIntent.SelectLastMatch -> handleSelectLastMatch(intent.matchId)
        }
    }

    private fun handleUpdatePlayer() {
        if (
            uiState.value.playerDetails?.status == uiState.value.selectedStatus &&
            uiState.value.playerDetails?.role == uiState.value.selectedRole
        ) {
            performAction(
                onSuccess = { effectsMutable.emit(PlayerDetailsEffect.PlayerUpdated) },
                action = {
                    updatePlayerUseCase.update(
                        playerId,
                        uiState.value.selectedStatus,
                        uiState.value.selectedRole
                    )
                }
            )
        }
    }

    private fun handleSelectLastMatch(matchId: String) {
        viewModelScope.launch {
            effectsMutable.emit(PlayerDetailsEffect.MatchSelected(matchId))
        }
    }

    private fun handleSelectStatus(staus: String) {
        uiStateMutable.update { it.copy(selectedStatus = UserStatus.valueOf(staus)) }
    }

    private fun handleSelectRole(role: String) {
        uiStateMutable.update { it.copy(selectedRole = UserRole.valueOf(role)) }
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
    val lastMatches: List<PlayerMatchPreview> = emptyList(),
    val selectedGroup: SelectedGroup? = null,
    val selectedStatus: UserStatus = UserStatus.ACTIVE,
    val selectedRole: UserRole = UserRole.PLAYER
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