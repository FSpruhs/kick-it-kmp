package com.spruhs.match.application

import com.spruhs.group.application.GroupRepository
import com.spruhs.user.application.SelectedGroup
import com.spruhs.user.application.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlin.collections.firstOrNull

class GetMatchResultDetailsUseCase(
    private val matchRepository: MatchRepository,
    private val userRepository: UserRepository,
    private val groupRepository: GroupRepository
) {
    suspend fun getData(matchId: String): Result = coroutineScope {
        val matchDeferred = async { matchRepository.getMatchById(matchId).first()}
        val selectedGroup = userRepository.getSelectedGroupOrThrow()
        val groupNameListDeferred = async { groupRepository.getGroupNames(selectedGroup.id)}
        val match = matchDeferred.await()

        val winnerTeam = match.result.firstOrNull()?.let { determineWinner(it) }
        val isDraw = match.result.firstOrNull()?.result == PlayerMatchResult.DRAW

        Result(
            playerResults = match.result,
            selectedGroup = selectedGroup,
            winnerTeam = winnerTeam,
            isDraw = isDraw,
            groupNameList = groupNameListDeferred.await().associate { it.id to it.name }
        )
    }

    private fun determineWinner(firstResult: PlayerResult): PlayerTeam? = when (firstResult.result) {
        PlayerMatchResult.DRAW -> null
        PlayerMatchResult.WIN -> firstResult.team
        PlayerMatchResult.LOSS -> firstResult.team.opposite()
    }

    data class Result(
        val playerResults: List<PlayerResult>,
        val selectedGroup: SelectedGroup,
        val winnerTeam: PlayerTeam?,
        val isDraw: Boolean,
        val groupNameList: Map<String, String>
    )
}