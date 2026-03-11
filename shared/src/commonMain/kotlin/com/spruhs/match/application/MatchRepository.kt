package com.spruhs.match.application

import kotlinx.datetime.LocalDateTime

interface MatchRepository {
    suspend fun getMatchesByGroup(
        groupId: String,
        after: LocalDateTime? = null,
        before: LocalDateTime? = null,
        userId: String? = null,
        limit: Int? = null
    ): List<Match>

    suspend fun getMatchById(matchId: String): Match

    suspend fun enterMatchResult(
        matchId: String,
        matchResult: MatchResult,
        teamA: List<String>,
        teamB: List<String>
    )

    suspend fun upcomingMatches(userId: String, after: LocalDateTime): List<UpcomingMatchPreview>

    suspend fun planMatch(command: PlanMatchCommand)
}