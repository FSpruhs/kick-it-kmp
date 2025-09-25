package com.spruhs.match.application

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

interface MatchRepository {
    suspend fun getMatchesByGroup(
        groupId: String,
        after: LocalDateTime? = null,
        before: LocalDateTime? = null,
        userId: String? = null,
        limit: Int? = null
    ): Flow<List<Match>>

    suspend fun upcomingMatches(userId: String): Collection<Match>
    suspend fun getMatchById(matchId: String): Flow<Match>
}