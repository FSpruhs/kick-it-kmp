package com.spruhs.user.application

import com.spruhs.dateTimeNow
import com.spruhs.match.application.MatchRepository
import com.spruhs.match.application.UpcomingMatchPreview

class GetUpcomingMatchesUseCase(private val matchRepository: MatchRepository) {
    suspend fun get(userId: String): List<UpcomingMatchPreview> =
        matchRepository.upcomingMatches(userId, dateTimeNow())
}