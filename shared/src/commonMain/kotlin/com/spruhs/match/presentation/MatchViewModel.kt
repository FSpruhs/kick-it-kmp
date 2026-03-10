package com.spruhs.match.presentation

import androidx.lifecycle.viewModelScope
import com.spruhs.BaseUIState
import com.spruhs.BaseViewModel
import com.spruhs.match.application.GetMatchesDataUseCases
import com.spruhs.match.application.PlayerMatchPreview
import com.spruhs.match.presentation.MatchEffect.*
import com.spruhs.user.application.SelectedGroup
import com.spruhs.user.application.UserGroupInfo
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MatchViewModel(private val getMatchesDataUseCases: GetMatchesDataUseCases) :
    BaseViewModel<MatchIntent, MatchEffect, MatchUIState>(MatchUIState()) {

    init {
        performAction(
            action = {
                getMatchesDataUseCases.getData()
            },
            onSuccess = { result ->
                uiStateMutable.update {
                    it.copy(
                        selectedGroup = result.selectedGroup,
                        matches = result.matches,
                        groups = result.groups
                    )
                }
            }
        )
    }

    override fun processIntent(intent: MatchIntent) {
        when (intent) {
            is MatchIntent.SelectMatch -> {
                viewModelScope.launch {
                    effectsMutable.emit(MatchSelected(intent.matchId))
                }
            }

            is MatchIntent.PlanMatch -> {
                viewModelScope.launch {
                    effectsMutable.emit(PlanMatchClicked)
                }
            }
        }
    }
}

data class MatchUIState(
    override val isLoading: Boolean = false,
    override val error: String? = null,
    val selectedGroup: SelectedGroup? = null,
    val matches: List<PlayerMatchPreview> = emptyList(),
    val groups: Map<String, UserGroupInfo> = emptyMap()
) : BaseUIState<MatchUIState> {
    override fun copyWith(isLoading: Boolean, error: String?): MatchUIState =
        copy(isLoading = isLoading, error = error)
}

sealed class MatchEffect {
    data class MatchSelected(val matchId: String) : MatchEffect()
    object PlanMatchClicked : MatchEffect()
}

sealed class MatchIntent {
    data class SelectMatch(val matchId: String) : MatchIntent()
    object PlanMatch : MatchIntent()
}