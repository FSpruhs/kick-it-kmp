package com.spruhs.match.presentation

import com.spruhs.BaseUIState
import com.spruhs.BaseViewModel
import com.spruhs.match.application.Match
import com.spruhs.user.application.SelectedGroup
import com.spruhs.user.application.UserGroupInfo

class MatchViewModel : BaseViewModel<MatchEffect, MatchUIState>(MatchUIState()) {
    fun processIntent(intent: MatchIntent) {
    }
}

data class MatchUIState(
    override val isLoading: Boolean = false,
    override val error: String? = null,
    val selectedGroup: SelectedGroup? = null,
    val upcomingMatches: List<Match> = emptyList(),
    val lastMatches: List<Match> = emptyList(),
    val groups: Map<String, UserGroupInfo> = emptyMap(),
    val userId: String? = null
) : BaseUIState<MatchUIState> {
    override fun copyWith(isLoading: Boolean, error: String?): MatchUIState =
        copy(isLoading = isLoading, error = error)
}

sealed class MatchEffect

sealed class MatchIntent {
    data class SelectMatch(val matchId: String) : MatchIntent()
}