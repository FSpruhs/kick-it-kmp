package com.spruhs.match.presentation

import com.spruhs.BaseViewModel
import com.spruhs.match.application.Match
import com.spruhs.match.application.PlayerMatchResult
import com.spruhs.match.application.PlayerTeam
import kotlinx.coroutines.flow.update

class EnterMatchResultViewModel : BaseViewModel<EnterMatchResultEffect, EnterMatchResultUIState>(EnterMatchResultUIState()) {
    fun processIntent(intent: EnterMatchResultIntent) {
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
        val matchData: Match = Match()
        val groupPlayers: Map<String, String> = mapOf()

            val winnerTeam: PlayerTeam =
                when (matchData.result.first().result) {
                    PlayerMatchResult.DRAW -> {
                        uiStateMutable.update { it.copy(isDraw = true) }
                        PlayerTeam.A
                    }

                    PlayerMatchResult.WIN -> if (matchData.result.first().team ==
                        PlayerTeam.A
                    ) {
                        PlayerTeam.A
                    } else {
                        PlayerTeam.B
                    }
                    PlayerMatchResult.LOSS -> if (matchData.result.first().team ==
                        PlayerTeam.A
                    ) {
                        PlayerTeam.B
                    } else {
                        PlayerTeam.A
                    }
                }
        val teamAPlayers = matchData.result.filter { it.team == PlayerTeam.A }.map { it.userId }
        val teamBPlayers = matchData.result.filter { it.team == PlayerTeam.B }.map { it.userId }

            val playerIds = (teamAPlayers) + (teamBPlayers)
            val noTeamPlayers =
                groupPlayers.keys
                    .filter { playerId ->
                        playerId !in playerIds
                    }
        uiStateMutable.update {
            it.copy(
                teamAPlayers = teamAPlayers,
                teamBPlayers = teamBPlayers,
                noTeamPlayers = noTeamPlayers,
                winnerTeam = winnerTeam,
                playerNames = groupPlayers
            )
        }
    }

    private fun movePlayer(to: Side) {
        val player = uiState.value.selectedPlayer
        val from = uiState.value.selectedSide
        if (player == null || from == null || from == to) return
        removePlayer(player, from)
        when (to) {
            Side.LEFT -> uiStateMutable.update { it.copy(teamAPlayers = it.teamAPlayers + player) }
            Side.MIDDLE -> uiStateMutable.update { it.copy(noTeamPlayers = it.noTeamPlayers + player) }
            Side.RIGHT -> uiStateMutable.update { it.copy(teamBPlayers = it.teamBPlayers + player) }
        }
    }

    private fun removePlayer(
        player: String?,
        from: Side?,
    ) {
        if (player == null || from == null) return
        when (from) {
            Side.LEFT -> uiStateMutable.update { it.copy(teamAPlayers = it.teamAPlayers - player) }
            Side.MIDDLE -> uiStateMutable.update { it.copy(noTeamPlayers = it.noTeamPlayers - player) }
            Side.RIGHT -> uiStateMutable.update { it.copy(teamBPlayers = it.teamBPlayers - player) }
        }
    }

}

data class EnterMatchResultUIState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isDraw: Boolean = false,
    val teamAPlayers: List<String> = emptyList(),
    val teamBPlayers: List<String> = emptyList(),
    val noTeamPlayers: List<String> = emptyList(),
    val winnerTeam: PlayerTeam = PlayerTeam.A,
    val playerNames: Map<String, String> = emptyMap(),
    val selectedPlayer: String? = null,
    val selectedSide: Side? = null,
)

enum class Side {
    LEFT,
    MIDDLE,
    RIGHT
}

sealed class EnterMatchResultEffect {
    object ResultEntered : EnterMatchResultEffect()
}

sealed class EnterMatchResultIntent {
    object EnterResult : EnterMatchResultIntent()
    data class UpdateIsDraw(val isDraw: Boolean) : EnterMatchResultIntent()
    data class SelectPlayer(val side: Side) : EnterMatchResultIntent()
    data class MovePlayer(val to: Side) : EnterMatchResultIntent()
}