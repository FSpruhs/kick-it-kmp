package com.spruhs.match.presentation

import androidx.lifecycle.viewModelScope
import com.spruhs.BaseUIState
import com.spruhs.BaseViewModel
import com.spruhs.match.application.GetMatchResultDetailsUseCase
import com.spruhs.match.application.PlayerResult
import com.spruhs.match.application.PlayerTeam
import com.spruhs.user.application.SelectedGroup
import kotlin.collections.List
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MatchResultDetailViewModel(
    private val matchId: String,
    private val getMatchResultDetailsUseCase: GetMatchResultDetailsUseCase
) : BaseViewModel<MatchResultDetailIntent, MatchResultDetailEffect, MatchResultDetailUIState>(
    MatchResultDetailUIState()
) {

    init {
        performAction(
            action = { getMatchResultDetailsUseCase.getData(matchId) },
            onSuccess = { result ->
                uiStateMutable.update {
                    it.copy(
                        playerResults = result.playerResults,
                        selectedGroup = result.selectedGroup,
                        winnerTeam = result.winnerTeam,
                        isDraw = result.isDraw,
                        groupNameList = result.groupNameList
                    )
                }
            }
        )
    }

    override fun processIntent(intent: MatchResultDetailIntent) {
        when (intent) {
            MatchResultDetailIntent.ChangeResult -> {
                viewModelScope.launch {
                    effectsMutable.emit(MatchResultDetailEffect.ChangeResult)
                }
            }
            MatchResultDetailIntent.EnterResult -> {
                viewModelScope.launch {
                    effectsMutable.emit(MatchResultDetailEffect.EnterResult)
                }
            }
        }
    }
}

data class MatchResultDetailUIState(
    override val isLoading: Boolean = false,
    override val error: String? = null,
    val playerResults: List<PlayerResult> = emptyList(),
    val selectedGroup: SelectedGroup? = null,
    val winnerTeam: PlayerTeam? = null,
    val isDraw: Boolean = false,
    val groupNameList: Map<String, String> = mapOf()
) : BaseUIState<MatchResultDetailUIState> {
    override fun copyWith(isLoading: Boolean, error: String?): MatchResultDetailUIState =
        copy(isLoading = isLoading, error = error)
}

sealed class MatchResultDetailEffect {
    object EnterResult : MatchResultDetailEffect()
    object ChangeResult : MatchResultDetailEffect()
}

sealed class MatchResultDetailIntent {
    object EnterResult : MatchResultDetailIntent()
    object ChangeResult : MatchResultDetailIntent()
}