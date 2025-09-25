package com.spruhs.match.application

import com.spruhs.group.application.GroupRepository
import com.spruhs.user.application.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first

class GetEnterResultDataUseCase(
    private val groupRepository: GroupRepository,
    private val userRepository: UserRepository,
    private val matchRepository: MatchRepository
) {
    suspend fun getData(matchId: String): Result {
        val (match, groupPlayers) = fetchData(matchId)
        val firstResult = match.result.firstOrNull()
        val winnerTeam = firstResult?.let { determineWinner(it) }

        return buildResult(groupPlayers, match.result.takeIf { firstResult != null }, winnerTeam)
    }


    private fun buildResult(
        groupPlayers: Map<String, String>,
        matchResults: List<PlayerResult>?,
        winnerTeam: PlayerTeam? = null
    ): Result {
        val teamAPlayers = matchResults?.let { getTeamPlayers(it, PlayerTeam.A) } ?: emptySet()
        val teamBPlayers = matchResults?.let { getTeamPlayers(it, PlayerTeam.B) } ?: emptySet()
        val playerIds = teamAPlayers + teamBPlayers
        val noTeamPlayers = getNoTeamPlayers(groupPlayers.keys.toSet(), playerIds)

        val isDraw = matchResults?.firstOrNull()?.result == PlayerMatchResult.DRAW

        return Result(
            isDraw = isDraw,
            teamAPlayers = teamAPlayers,
            teamBPlayers = teamBPlayers,
            noTeamPlayers = noTeamPlayers,
            winnerTeam = winnerTeam ?: PlayerTeam.A,
            playerNames = groupPlayers
        )
    }

    private suspend fun fetchData(matchId: String): Pair<Match, Map<String, String>> = coroutineScope {
        val selectedGroupDeferred = async { userRepository.getSelectedGroupOrThrow() }
        val matchDeferred = async { matchRepository.getMatchById(matchId).first() }

        val selectedGroup = selectedGroupDeferred.await()
        val groupPlayers = groupRepository.getGroupNames(selectedGroup.id).associate { it.id to it.name }

        val match = matchDeferred.await()

        match to groupPlayers
    }

    private fun determineWinner(firstResult: PlayerResult): PlayerTeam =
        when (firstResult.result) {
            PlayerMatchResult.DRAW -> PlayerTeam.A
            PlayerMatchResult.WIN -> firstResult.team
            PlayerMatchResult.LOSS -> firstResult.team.opposite()
        }

    private fun getNoTeamPlayers(allPlayerIds: Set<String>, teamPlayers: Set<String>): Set<String> =
        allPlayerIds - teamPlayers

    private fun getTeamPlayers(results: List<PlayerResult>, team: PlayerTeam): Set<String> =
        results.filter { it.team == team }.map { it.userId }.toSet()

    data class Result(
        val isDraw: Boolean,
        val teamAPlayers: Set<String>,
        val teamBPlayers: Set<String>,
        val noTeamPlayers: Set<String>,
        val winnerTeam: PlayerTeam,
        val playerNames: Map<String, String>,
    )
}