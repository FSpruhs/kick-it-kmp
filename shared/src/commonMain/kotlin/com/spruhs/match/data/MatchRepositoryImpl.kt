package com.spruhs.match.data

import com.spruhs.match.application.Match
import com.spruhs.match.application.MatchRepository
import com.spruhs.match.application.MatchResult
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

class MatchRepositoryImpl : MatchRepository {
    override suspend fun getMatchesByGroup(
        groupId: String,
        after: LocalDateTime?,
        before: LocalDateTime?,
        userId: String?,
        limit: Int?
    ): Flow<List<Match>> {
        TODO("Not yet implemented")
    }

    override suspend fun getMatchById(matchId: String): Flow<Match> {
        TODO("Not yet implemented")
    }

    override suspend fun enterMatchResult(
        matchId: String,
        matchResult: MatchResult,
        teamA: List<String>,
        teamB: List<String>
    ) {
        TODO("Not yet implemented")
    }
}