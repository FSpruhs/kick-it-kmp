package com.spruhs.user.presentation

import androidx.lifecycle.viewModelScope
import com.spruhs.AppLogger
import com.spruhs.BaseViewModel
import com.spruhs.match.application.Match
import com.spruhs.match.application.MatchRepository
import com.spruhs.user.application.UserGroupInfo
import com.spruhs.user.application.UserRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userRepository: UserRepository,
    private val matchRepository: MatchRepository
) : BaseViewModel<HomeEffect, HomeUIState>(HomeUIState()) {

    init {
        loadState()
    }

    private fun loadState() {
        viewModelScope.launch {
            val user = userRepository.userState.firstOrNull()
            if (user != null) {
                uiStateMutable.update { state ->
                    state.copy(
                        userId = user.userId,
                        groups = user.groups,
                        upcomingMatches = fetchUpcomingMatches(user.userId)
                    )
                }
            } else {
                AppLogger.e("HomeViewModel", "User not found in state")
                uiStateMutable.update { it.copy(error = "User not found") }
            }
        }
    }

    private suspend fun fetchUpcomingMatches(userId: String) = runCatching {
        matchRepository.upcomingMatches(userId)
    }.getOrElse {
        AppLogger.e("HomeViewModel", "Error fetching upcoming matches", it)
        emptyList()
    }.toList()

    fun processIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.SelectMatch -> viewModelScope.launch {
                effectsMutable.emit(HomeEffect.MatchSelected(intent.matchId))
            }
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