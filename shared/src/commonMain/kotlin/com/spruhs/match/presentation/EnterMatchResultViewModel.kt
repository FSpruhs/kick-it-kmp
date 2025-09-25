package com.spruhs.match.presentation

import com.spruhs.BaseUIState
import com.spruhs.BaseViewModel
import com.spruhs.match.application.GetEnterResultDataUseCase
import com.spruhs.match.application.PlayerTeam
import kotlinx.coroutines.flow.update

class EnterMatchResultViewModel(
    private val matchId: String,
    private val getEnterResultDataUseCase: GetEnterResultDataUseCase
) :
    BaseViewModel<EnterMatchResultIntent, EnterMatchResultEffect, EnterMatchResultUIState>(
        EnterMatchResultUIState()
    ) {

    override fun processIntent(intent: EnterMatchResultIntent) {
        when (intent) {
            is EnterMatchResultIntent.EnterResult -> {}
            is EnterMatchResultIntent.UpdateIsDraw -> {}
            is EnterMatchResultIntent.SelectPlayer -> {}
            is EnterMatchResultIntent.MovePlayer -> {
                movePlayer(intent.to)
                uiStateMutable.update { it.copy(selectedPlayer = null, selectedSide = null) }
            }
        }
    }

    init {
        performAction(
            action = {
                getEnterResultDataUseCase.getData(matchId)
            },
            onSuccess = { result ->
                uiStateMutable.update {
                    it.copy(
                        teamAPlayers = result.teamAPlayers,
                        teamBPlayers = result.teamBPlayers,
                        noTeamPlayers = result.noTeamPlayers,
                        winnerTeam = result.winnerTeam,
                        playerNames = result.playerNames
                    )
                }
            }
        )
    }

    private fun movePlayer(to: Side) {
        val player = uiState.value.selectedPlayer
        val from = uiState.value.selectedSide
        if (player == null || from == null || from == to) return
        removePlayer(player, from)
        when (to) {
            Side.LEFT -> uiStateMutable.update { it.copy(teamAPlayers = it.teamAPlayers + player) }
            Side.MIDDLE -> uiStateMutable.update {
                it.copy(noTeamPlayers = it.noTeamPlayers + player)
            }
            Side.RIGHT -> uiStateMutable.update { it.copy(teamBPlayers = it.teamBPlayers + player) }
        }
    }

    private fun removePlayer(player: String?, from: Side?) {
        if (player == null || from == null) return
        when (from) {
            Side.LEFT -> uiStateMutable.update { it.copy(teamAPlayers = it.teamAPlayers - player) }
            Side.MIDDLE -> uiStateMutable.update {
                it.copy(noTeamPlayers = it.noTeamPlayers - player)
            }
            Side.RIGHT -> uiStateMutable.update { it.copy(teamBPlayers = it.teamBPlayers - player) }
        }
    }
}

data class EnterMatchResultUIState(
    override val isLoading: Boolean = false,
    override val error: String? = null,
    val isDraw: Boolean = false,
    val teamAPlayers: Set<String> = emptySet(),
    val teamBPlayers: Set<String> = emptySet(),
    val noTeamPlayers: Set<String> = emptySet(),
    val winnerTeam: PlayerTeam = PlayerTeam.A,
    val playerNames: Map<String, String> = emptyMap(),
    val selectedPlayer: String? = null,
    val selectedSide: Side? = null
) : BaseUIState<EnterMatchResultUIState> {
    override fun copyWith(isLoading: Boolean, error: String?): EnterMatchResultUIState =
        copy(isLoading = isLoading, error = error)
}

enum class Side {
    LEFT,
    MIDDLE,
    RIGHT
}

sealed class EnterMatchResultEffect {
    object ResultEntered : EnterMatchResultEffect()
    data class ShowError(val message: String) : EnterMatchResultEffect()
}

sealed class EnterMatchResultIntent {
    object EnterResult : EnterMatchResultIntent()
    data class UpdateIsDraw(val isDraw: Boolean) : EnterMatchResultIntent()
    data class SelectPlayer(val side: Side) : EnterMatchResultIntent()
    data class MovePlayer(val to: Side) : EnterMatchResultIntent()
}