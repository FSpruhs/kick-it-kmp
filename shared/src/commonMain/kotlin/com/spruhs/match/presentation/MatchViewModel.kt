package com.spruhs.match.presentation

import androidx.lifecycle.viewModelScope
import com.spruhs.BaseUIState
import com.spruhs.BaseViewModel
import com.spruhs.match.application.GetMatchesDataUseCases
import com.spruhs.match.application.PlayerMatchPreview
import com.spruhs.match.application.UpcomingMatchPreview
import com.spruhs.user.application.SelectedGroup
import com.spruhs.user.application.UserGroupInfo
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MatchViewModel(
    private val getMatchesDataUseCases: GetMatchesDataUseCases
) : BaseViewModel<MatchIntent, MatchEffect, MatchUIState>(MatchUIState()) {

    init {
        performAction(
            action = {
                getMatchesDataUseCases.getData()
            },
            onSuccess = { result ->
                uiStateMutable.update { it.copy(
                    selectedGroup = result.selectedGroup,
                    upcomingMatches = result.upcomingMatches,
                    lastMatches = result.lastMatches,
                    groups = result.groups
                ) }
            }
        )
    }

    override fun processIntent(intent: MatchIntent) {
        when (intent) {
            is MatchIntent.SelectLastMatch -> {
                viewModelScope.launch {
                    effectsMutable.emit(MatchEffect.LastMatchSelected(intent.matchId))
                }
            }
            is MatchIntent.SelectUpcomingMatch -> {
                viewModelScope.launch {
                    effectsMutable.emit(MatchEffect.UpcomingMatchSelected(intent.matchId))
                }
            }
        }
    }
}

data class MatchUIState(
    override val isLoading: Boolean = false,
    override val error: String? = null,
    val selectedGroup: SelectedGroup? = null,
    val upcomingMatches: List<UpcomingMatchPreview> = emptyList(),
    val lastMatches: List<PlayerMatchPreview> = emptyList(),
    val groups: Map<String, UserGroupInfo> = emptyMap(),
) : BaseUIState<MatchUIState> {
    override fun copyWith(isLoading: Boolean, error: String?): MatchUIState =
        copy(isLoading = isLoading, error = error)
}

sealed class MatchEffect {
    data class UpcomingMatchSelected(val matchId: String) : MatchEffect()
    data class LastMatchSelected(val matchId: String) : MatchEffect()
}

sealed class MatchIntent {
    data class SelectUpcomingMatch(val matchId: String) : MatchIntent()
    data class SelectLastMatch(val matchId: String) : MatchIntent()
}