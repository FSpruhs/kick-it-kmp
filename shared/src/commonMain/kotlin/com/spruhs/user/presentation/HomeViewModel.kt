package com.spruhs.user.presentation

import androidx.lifecycle.viewModelScope
import com.spruhs.AppLogger
import com.spruhs.BaseUIState
import com.spruhs.BaseViewModel
import com.spruhs.match.application.UpcomingMatchPreview
import com.spruhs.user.application.GetUpcomingMatchesUseCase
import com.spruhs.user.application.LoadUserUseCase
import com.spruhs.user.application.UserGroupInfo
import kotlin.collections.emptyList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val loadUserUseCase: LoadUserUseCase,
    private val getUpcomingMatchesUseCase: GetUpcomingMatchesUseCase
) : BaseViewModel<HomeIntent, HomeEffect, HomeUIState>(HomeUIState()) {

    init {
        loadState()
    }

    private fun loadState() {
        viewModelScope.launch {
            val user = loadUserUseCase.getUser()
            if (user != null) {
                uiStateMutable.update { state ->
                    state.copy(
                        userId = user.userId,
                        groups = user.groups,
                        upcomingMatches = fetchUpcomingMatches(user.userId)
                            .sortedByDescending { it.start }
                    )
                }
            } else {
                AppLogger.e("HomeViewModel", "User not found in state")
                effectsMutable.emit(HomeEffect.ShowError("User not found in state"))
            }
        }
    }

    private suspend fun fetchUpcomingMatches(userId: String): List<UpcomingMatchPreview> =
        runCatching {
            getUpcomingMatchesUseCase.get(userId)
        }.getOrElse {
            AppLogger.e("HomeViewModel", "Error fetching upcoming matches", it)
            emptyList()
        }

    override fun processIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.SelectMatch -> viewModelScope.launch {
                effectsMutable.emit(HomeEffect.MatchSelected(intent.matchId))
            }
        }
    }
}

data class HomeUIState(
    override val isLoading: Boolean = false,
    override val error: String? = null,
    val upcomingMatches: List<UpcomingMatchPreview> = emptyList(),
    val groups: Map<String, UserGroupInfo> = emptyMap(),
    val userId: String? = null
) : BaseUIState<HomeUIState> {
    override fun copyWith(isLoading: Boolean, error: String?): HomeUIState =
        copy(isLoading = isLoading, error = error)
}

sealed class HomeIntent {
    data class SelectMatch(val matchId: String) : HomeIntent()
}

sealed class HomeEffect {
    data class MatchSelected(val matchId: String) : HomeEffect()
    data class ShowError(val message: String) : HomeEffect()
}