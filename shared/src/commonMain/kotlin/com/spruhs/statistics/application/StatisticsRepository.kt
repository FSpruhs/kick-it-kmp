package com.spruhs.statistics.application

interface StatisticsRepository {
    suspend fun findByUserId(userId: String, groupId: String): PlayerStats
}