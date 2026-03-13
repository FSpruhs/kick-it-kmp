package com.spruhs.statistics.data

import com.spruhs.statistics.application.PlayerStats
import com.spruhs.statistics.application.StatisticsRepository
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import kotlinx.serialization.Serializable

class StatisticsRepositoryImpl(private val statisticsApi: StatisticsApi) : StatisticsRepository {
    override suspend fun findByUserId(userId: String, groupId: String): PlayerStats =
        statisticsApi.findByUserId(userId, groupId).toPlayerStats()
}

interface StatisticsApi {
    @GET("v1/statistic/group/{groupId}/player/{userId}")
    suspend fun findByUserId(
        @Path("userId") userId: String,
        @Path("groupId") groupId: String
    ): PlayerStatisticMessage
}

private fun PlayerStatisticMessage.toPlayerStats() = PlayerStats(
    totalMatches = totalMatches,
    wins = wins,
    losses = losses,
    draws = draws
)

@Serializable
data class PlayerStatisticMessage(
    val groupId: String,
    val userId: String,
    val totalMatches: Int,
    val wins: Int,
    val losses: Int,
    val draws: Int
)