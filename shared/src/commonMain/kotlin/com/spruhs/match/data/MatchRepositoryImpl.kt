package com.spruhs.match.data

import com.spruhs.match.application.Match
import com.spruhs.match.application.MatchRepository
import com.spruhs.match.application.MatchResult
import com.spruhs.match.application.PlayerStatus
import com.spruhs.match.application.UpcomingMatchPreview
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

class MatchRepositoryImpl(private val matchApiClient: MatchApiClient) : MatchRepository {
    override suspend fun getMatchesByGroup(
        groupId: String,
        after: LocalDateTime?,
        before: LocalDateTime?,
        userId: String?,
        limit: Int?
    ): Flow<List<Match>> = flow {
        matchApiClient.getMatchesByGroup(
            groupId = groupId,
            after = after,
            before = before,
            userId = userId,
            limit = limit
        )
    }

    override suspend fun getMatchById(matchId: String): Flow<Match> = flow {
        matchApiClient.getMatchById(matchId)
    }

    override suspend fun enterMatchResult(
        matchId: String,
        matchResult: MatchResult,
        teamA: List<String>,
        teamB: List<String>
    ) {
        val teamAResult = teamA.map {
            PlayerMatchResult(it, calculateResult("A", matchResult), "A")
        }
        val teamBResult = teamA.map {
            PlayerMatchResult(it, calculateResult("B", matchResult), "B")
        }
        matchApiClient.enterMatchResult(matchId, EnterResultRequest(teamAResult + teamBResult))
    }

    private fun calculateResult(team: String, matchResult: MatchResult) = when (matchResult) {
        MatchResult.DRAW -> "DRAW"
        MatchResult.TEAM_A -> if (team == "A") "WIN" else "LOSS"
        MatchResult.TEAM_B -> if (team == "B") "WIN" else "LOSS"
    }

    override suspend fun upcomingMatches(
        userId: String,
        after: LocalDateTime
    ): List<UpcomingMatchPreview> = matchApiClient.getMatchesByPlayer(userId, after).map {
        UpcomingMatchPreview(
            id = it.id,
            groupId = it.groupId,
            cadre = it.cadrePlayers.map { player -> player.userId },
            minPlayers = it.minPlayer,
            maxPlayers = it.maxPlayer,
            start = it.start,
            playerStatus = toPlayerStatus(it, userId)
        )
    }

    private fun toPlayerStatus(response: MatchResponse, userId: String): PlayerStatus? {
        val cadre = response.cadrePlayers.map { it.userId }.find { it == userId }
        if (cadre != null) return PlayerStatus.CADRE

        val deregistered = response.deregisteredPlayers.map { it.userId }.find { it == userId }
        if (deregistered != null) return PlayerStatus.DEREGISTERED

        val waitingBench = response.waitingBenchPlayers.map { it.userId }.find { it == userId }
        if (waitingBench != null) return PlayerStatus.WAITING_BENCH

        return null
    }
}

interface MatchApiClient {
    @GET("v1/match/{matchId}")
    suspend fun getMatchById(@Path("matchId")matchId: String): MatchResponse

    @GET("v1/match/group/{groupId}")
    suspend fun getMatchesByGroup(
        @Path("groupId") groupId: String,
        @Query("after") after: LocalDateTime?,
        @Query("before") before: LocalDateTime?,
        @Query("userId") userId: String?,
        @Query("limit") limit: Int?
    ): List<MatchResponse>

    @GET("v1/match/player/{playerId}")
    suspend fun getMatchesByPlayer(
        @Path("playerId") playerId: String,
        @Query("after") after: LocalDateTime?
    ): List<MatchResponse>

    @POST("v1/match/{matchId}/result")
    suspend fun enterMatchResult(
        @Path("matchId") matchId: String,
        @Body matchResult: EnterResultRequest
    )
}

@Serializable
data class MatchResponse(
    val id: String,
    val groupId: String,
    val start: LocalDateTime,
    val playground: String? = null,
    val maxPlayer: Int,
    val minPlayer: Int,
    val isCanceled: Boolean,
    val cadrePlayers: Set<RegisteredPlayerInfoMessage>,
    val deregisteredPlayers: Set<RegisteredPlayerInfoMessage>,
    val waitingBenchPlayers: Set<RegisteredPlayerInfoMessage>,
    val result: List<PlayerResultMessage>
)

@Serializable
data class RegisteredPlayerInfoMessage(val userId: String, val guestOf: String?)

@Serializable
data class PlayerResultMessage(val userId: String, val result: String, val team: String)

@Serializable
data class EnterResultRequest(val players: List<PlayerMatchResult>)

@Serializable
data class PlayerMatchResult(val userId: String, val result: String, val team: String)