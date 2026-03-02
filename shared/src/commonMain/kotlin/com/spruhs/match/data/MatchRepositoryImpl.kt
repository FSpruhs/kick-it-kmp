package com.spruhs.match.data

import com.spruhs.match.application.Match
import com.spruhs.match.application.MatchRepository
import com.spruhs.match.application.MatchResult
import com.spruhs.match.application.PlayerStatus
import com.spruhs.match.application.UpcomingMatchPreview
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

class MatchRepositoryImpl(private val matchService: MatchService) : MatchRepository {
    override suspend fun getMatchesByGroup(
        groupId: String,
        after: LocalDateTime?,
        before: LocalDateTime?,
        userId: String?,
        limit: Int?
    ): Flow<List<Match>> {
        return flow { matchService.getMatchesByGroup(
            groupId = groupId,
            after = after,
            before = before,
            userId = userId,
            limit = limit
        ) }
    }

    override suspend fun getMatchById(matchId: String): Flow<Match> {
        return flow { matchService.getMatchById(matchId) }
    }

    override suspend fun enterMatchResult(
        matchId: String,
        matchResult: MatchResult,
        teamA: List<String>,
        teamB: List<String>
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun upcomingMatches(userId: String, after: LocalDateTime): List<UpcomingMatchPreview> {
        return matchService.getMatchesByPlayer(userId, after).map { UpcomingMatchPreview(
            id = it.id,
            groupId = it.groupId,
            cadre = it.cadrePlayers.map { it.userId },
            minPlayers = it.minPlayer,
            maxPlayers = it.maxPlayer,
            start = it.start.toString(),
            playerStatus = PlayerStatus.CADRE
        ) }
    }
}

interface MatchService {
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
    val result: List<PlayerResultMessage>,
)

@Serializable
data class RegisteredPlayerInfoMessage(val userId: String, val guestOf: String?)

@Serializable
data class PlayerResultMessage(
    val userId: String,
    val result: String,
    val team: String
)