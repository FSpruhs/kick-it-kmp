package com.spruhs.match.application

import com.spruhs.dateTimeNow
import com.spruhs.user.application.UserRepository
import kotlinx.datetime.LocalDateTime

class PlanMatchUseCase(
    private val matchRepository: MatchRepository,
    private val userRepository: UserRepository
) {

    suspend fun plan(
        start: LocalDateTime,
        location: String,
        minPlayers: Int,
        maxPlayers: Int
    ) {
        if (start <= dateTimeNow()) {
            throw IllegalArgumentException("Start time must be in the future")
        }

        matchRepository.planMatch(PlanMatchCommand(
            groupId = userRepository.selectedGroup.value?.id ?: throw IllegalStateException("No group selected"),
            start = start,
            location = location.ifEmpty { null },
            minPlayers = minPlayers,
            maxPlayers = maxPlayers
        ))
    }
}

data class PlanMatchCommand(
    val groupId: String,
    val start: LocalDateTime,
    val location: String?,
    val minPlayers: Int,
    val maxPlayers: Int
)