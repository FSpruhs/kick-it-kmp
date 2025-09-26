package com.spruhs.match.application

class EnterResultUseCase(private val matchRepository: MatchRepository) {
    suspend fun enterResult(
        matchId: String,
        result: MatchResult,
        teamA: List<String>,
        teamB: List<String>
    ) {
        matchRepository.enterMatchResult(
            matchId = matchId,
            matchResult = result,
            teamA = teamA,
            teamB = teamB
        )
    }
}