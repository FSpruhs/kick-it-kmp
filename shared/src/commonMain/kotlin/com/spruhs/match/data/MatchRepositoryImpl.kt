package com.spruhs.match.data

import com.spruhs.match.application.Match
import com.spruhs.match.application.MatchRepository

class MatchRepositoryImpl : MatchRepository {
    override suspend fun upcomingMatches(userId: String): Collection<Match> {
        TODO("Not yet implemented")
    }
}