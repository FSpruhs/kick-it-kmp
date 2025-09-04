package com.spruhs.user.presentation

import androidx.lifecycle.viewModelScope
import com.spruhs.BaseViewModel
import com.spruhs.match.application.Match
import com.spruhs.user.application.UserGroupInfo
import kotlinx.coroutines.launch

class HomeViewModel : BaseViewModel<HomeEffect, HomeUIState>(HomeUIState()) {

    fun processIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.SelectMatch -> viewModelScope.launch { effectsMutable.emit(HomeEffect.MatchSelected(intent.matchId)) }
        }
    }

}

data class HomeUIState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val upcomingMatches: List<Match> = emptyList(),
    val groups: Map<String, UserGroupInfo> = emptyMap(),
    val userId: String? = null
)

sealed class HomeIntent {
    data class SelectMatch(val matchId: String) : HomeIntent()
}

sealed class HomeEffect {
    data class MatchSelected(val matchId: String) : HomeEffect()
}