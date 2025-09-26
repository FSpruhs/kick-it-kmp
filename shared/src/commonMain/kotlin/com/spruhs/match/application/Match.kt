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

fun Match.toUpcomingPreview(playerId: String) = UpcomingMatchPreview(
    id = id,
    groupId = groupId,
    cadre = cadre,
    minPlayers = minPlayers,
    maxPlayers = maxPlayers,
    start = start.toString(),
    playerStatus = calculatePlayerStatus(this, playerId),
)

private fun calculatePlayerStatus(match: Match, userId: String) = when {
    match.cadre.find { it == userId } != null -> PlayerStatus.CADRE
    match.deregistered.find { it == userId } != null -> PlayerStatus.DEREGISTERED
    match.waitingBench.find { it == userId } != null -> PlayerStatus.WAITING_BENCH

    else -> null
}

fun Match.toLastPreview(playerId: String) =
    PlayerMatchPreview(
        id = id,
        playerResult = this.result.find { it.userId == playerId }?.result ?: PlayerMatchResult.DRAW,
        start = start.toString(),
        playground = playground
    )

data class UpcomingMatchPreview(
    val id: String,
    val groupId: String,
    val cadre: List<String>,
    val minPlayers: Int,
    val maxPlayers: Int,
    val start: String,
    val playerStatus: PlayerStatus?
)

data class PlayerMatchPreview(
    val id: String,
    val playerResult: PlayerMatchResult,
    val start: String,
    val playground: String?
)

data class PlayerResult(val userId: String, val result: PlayerMatchResult, val team: PlayerTeam)

enum class MatchResult {
    DRAW,
    TEAM_A,
    TEAM_B
}

enum class PlayerMatchResult {
    WIN,
    LOSS,
    DRAW
}

enum class PlayerTeam {
    A,
    B;

    fun opposite(): PlayerTeam = when (this) {
        A -> B
        B -> A
    }
}

enum class PlayerStatus {
    CADRE,
    WAITING_BENCH,
    DEREGISTERED
}