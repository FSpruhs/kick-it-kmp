package com.spruhs.match.application

import com.spruhs.dateTimeNow
import com.spruhs.user.application.SelectedGroup
import com.spruhs.user.application.UserGroupInfo
import com.spruhs.user.application.UserRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull

class GetMatchesDataUseCases(
    private val matchRepository: MatchRepository,
    private val userRepository: UserRepository
) {
    suspend fun getData(): Result = coroutineScope {
        val selectedGroup = userRepository.getSelectedGroupOrThrow()
        val user = userRepository.getUserOrThrow()
        val matches = fetchMatches(selectedGroup.id)

        Result(
            selectedGroup = selectedGroup,
            matches = matches.map { it.toPreview(user.userId) }.sortedByDescending { it.start },
            groups = user.groups
        )
    }

    private suspend fun fetchMatches(groupId: String) = matchRepository.getMatchesByGroup(
        groupId,
        null,
        dateTimeNow(),
        null,
        null
    )

    data class Result(
        val selectedGroup: SelectedGroup?,
        val matches: List<PlayerMatchPreview>,
        val groups: Map<String, UserGroupInfo>
    )
}