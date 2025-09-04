package com.spruhs.match.application

import kotlinx.datetime.LocalDateTime

data class Match(
    val id: String,
    val groupId: String,
    val start: LocalDateTime,
    val playground: String? = null,
    val maxPlayers: Int,
    val minPlayers: Int,
    val cadre: List<String> = emptyList(),
    val waitingBench: List<String> = emptyList(),
    val deregistered: List<String> = emptyList(),
    val result: List<PlayerResult>
)

data class PlayerResult(val userId: String, val result: PlayerMatchResult, val team: PlayerTeam)

enum class PlayerMatchResult {
    WIN,
    LOSS,
    DRAW
}

enum class PlayerTeam {
    A,
    B
}

enum class PlayerStatus {
    CADRE,
    WAITING_BENCH,
    DEREGISTERED
}