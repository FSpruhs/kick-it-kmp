package com.spruhs.match.application

interface MatchRepository {
    suspend fun upcomingMatches(userId: String): Collection<Match>
}