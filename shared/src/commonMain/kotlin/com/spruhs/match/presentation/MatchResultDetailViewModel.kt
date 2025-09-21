package com.spruhs.match.presentation

import com.spruhs.BaseUIState
import com.spruhs.BaseViewModel
import com.spruhs.match.application.PlayerMatchResult
import com.spruhs.match.application.PlayerResult
import com.spruhs.match.application.PlayerTeam
import com.spruhs.user.application.SelectedGroup
import kotlinx.coroutines.flow.update

class MatchResultDetailViewModel :
    BaseViewModel<MatchResultDetailEffect, MatchResultDetailUIState>(MatchResultDetailUIState()) {

    init {
        val winnerTeam: PlayerTeam =
            when (
                uiState.value.playerResults
                    .first()
                    .result
            ) {
                PlayerMatchResult.DRAW -> PlayerTeam.A
                PlayerMatchResult.WIN ->
                    if (uiState.value.playerResults
                            .first()
                            .team == PlayerTeam.A
                    ) {
                        PlayerTeam.A
                    } else {
                        PlayerTeam.B
                    }

                PlayerMatchResult.LOSS ->
                    if (uiState.value.playerResults
                            .first()
                            .team == PlayerTeam.A
                    ) {
                        PlayerTeam.B
                    } else {
                        PlayerTeam.A
                    }
            }
        uiStateMutable.update { it.copy(winnerTeam = winnerTeam) }
    }

    fun processIntent(intent: MatchResultDetailIntent) {
    }
}

data class MatchResultDetailUIState(
    override val isLoading: Boolean = false,
    val playerResults: List<PlayerResult> = emptyList(),
    val selectedGroup: SelectedGroup? = null,
    val winnerTeam: PlayerTeam? = null,
    val isDraw: Boolean = false,
    val groupNameList: Map<String, String> = mapOf()
) : BaseUIState<MatchResultDetailUIState> {
    override fun copyWith(isLoading: Boolean): MatchResultDetailUIState {
        return copy(isLoading = isLoading)
    }
}

sealed class MatchResultDetailEffect

sealed class MatchResultDetailIntent {
    object EnterResult : MatchResultDetailIntent()
    object ChangeResult : MatchResultDetailIntent()
}