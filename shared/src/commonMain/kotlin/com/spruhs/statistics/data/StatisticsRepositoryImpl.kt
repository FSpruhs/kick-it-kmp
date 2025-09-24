package com.spruhs.statistics.data

import com.spruhs.statistics.application.PlayerStats
import com.spruhs.statistics.application.StatisticsRepository

class StatisticsRepositoryImpl : StatisticsRepository  {
    override suspend fun findByUserId(
        userId: String,
        groupId: String
    ): PlayerStats {
        TODO("Not yet implemented")
    }
}