package com.spruhs.group.presentation

import com.spruhs.BaseUIState
import com.spruhs.BaseViewModel
import com.spruhs.group.application.PlayerDetails
import com.spruhs.match.application.Match
import com.spruhs.statistics.application.PlayerStats
import com.spruhs.user.application.SelectedGroup
import com.spruhs.user.application.UserRole
import com.spruhs.user.application.UserStatus

class PlayerDetailsViewModel :
    BaseViewModel<PlayerDetailsEffect, PlayerDetailsUIState>(PlayerDetailsUIState()) {
    fun processIntent(intent: PlayerDetailsIntent) {
        when (intent) {
            is PlayerDetailsIntent.RemovePlayer -> {}
            is PlayerDetailsIntent.UpdatePlayer -> TODO()
            is PlayerDetailsIntent.SelectRole -> TODO()
            is PlayerDetailsIntent.SelectStatus -> TODO()
            is PlayerDetailsIntent.SelectLastMatch -> TODO()
        }
    }
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
    override fun copyWith(isLoading: Boolean): PlayerDetailsUIState = copy(isLoading = isLoading)
}

sealed class PlayerDetailsEffect {
    object PlayerRemoved : PlayerDetailsEffect()
    object PlayerUpdated : PlayerDetailsEffect()
}

sealed class PlayerDetailsIntent {
    object RemovePlayer : PlayerDetailsIntent()
    object UpdatePlayer : PlayerDetailsIntent()
    data class SelectStatus(val status: String) : PlayerDetailsIntent()
    data class SelectRole(val role: String) : PlayerDetailsIntent()
    data class SelectLastMatch(val matchId: String) : PlayerDetailsIntent()
}