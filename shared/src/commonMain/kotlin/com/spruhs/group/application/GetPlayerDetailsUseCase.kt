package com.spruhs.group.application

import com.spruhs.match.application.Match
import com.spruhs.match.application.MatchRepository
import com.spruhs.statistics.application.PlayerStats
import com.spruhs.statistics.application.StatisticsRepository
import com.spruhs.user.application.SelectedGroup
import com.spruhs.user.application.UserRepository
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class GetPlayerDetailsUseCase(
    private val userRepository: UserRepository,
    private val groupRepository: GroupRepository,
    private val statisticsRepository: StatisticsRepository,
    private val matchRepository: MatchRepository
) {
    @OptIn(ExperimentalTime::class)
    suspend fun getPlayerDetails(playerId: String): Result {
        val selectedGroup = userRepository.getSelectedGroupOrThrow()
        return coroutineScope {
            val statisticDeferred =
                async { statisticsRepository.findByUserId(playerId, selectedGroup.id) }
            val playerDeferred = async { groupRepository.getPlayer(selectedGroup.id, playerId) }
            val groupNamesDeferred = async { groupRepository.getGroupNames(selectedGroup.id) }
            val lastMatchesDeferred = async {
                matchRepository.getMatchesByGroup(
                    selectedGroup.id,
                    null,
                    Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                    playerId,
                    3
                ).first()
            }

            Result(
                statistics = statisticDeferred.await(),
                player = playerDeferred.await(),
                groupNames = groupNamesDeferred.await().associate { it.id to it.name },
                lastMatches = lastMatchesDeferred.await(),
                selectedGroup = selectedGroup
            )
        }
    }

    data class Result(
        val statistics: PlayerStats,
        val player: PlayerDetails,
        val groupNames: Map<String, String>,
        val lastMatches: List<Match>,
        val selectedGroup: SelectedGroup
    )
}