package com.spruhs.match.application

import com.spruhs.dateTimeNow
import com.spruhs.user.application.SelectedGroup
import com.spruhs.user.application.UserGroupInfo
import com.spruhs.user.application.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first

class GetMatchesDataUseCases(
    private val matchRepository: MatchRepository,
    private val userRepository: UserRepository
) {
    suspend fun getData(): Result = coroutineScope {
        val selectedGroup = userRepository.getSelectedGroupOrThrow()

        val upcomingMatchesDeferred = async { fetchUpcomingMatches(selectedGroup.id) }
        val lastMatchesDeferred = async { fetchLastMatches(selectedGroup.id) }
        val user = userRepository.getUserOrThrow()

        val upcomingMatches = upcomingMatchesDeferred.await()
        val lastMatches = lastMatchesDeferred.await()

        Result(
            selectedGroup = selectedGroup,
            upcomingMatches = upcomingMatches.map { it.toUpcomingPreview(user.userId) },
            lastMatches = lastMatches.map { it.toLastPreview(user.userId) },
            groups = user.groups
        )
    }

    private suspend fun fetchUpcomingMatches(groupId: String) = matchRepository.getMatchesByGroup(
        groupId,
        dateTimeNow(),
        null,
        null,
        null
    ).first()

    private suspend fun fetchLastMatches(groupId: String) = matchRepository.getMatchesByGroup(
        groupId,
        null,
        dateTimeNow(),
        null,
        null
    ).first()

    data class Result(
        val selectedGroup: SelectedGroup?,
        val upcomingMatches: List<UpcomingMatchPreview>,
        val lastMatches: List<PlayerMatchPreview>,
        val groups: Map<String, UserGroupInfo>,
    )
}