package com.spruhs.match.presentation

import com.spruhs.BaseUIState
import com.spruhs.BaseViewModel
import com.spruhs.match.application.Match
import com.spruhs.match.application.PlayerStatus
import com.spruhs.user.application.UserRole
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDateTime

class UpcomingMatchDetailsViewModel :
    BaseViewModel<UpcomingMatchDetailsIntent, UpcomingMatchDetailsEffect, UpcomingMatchDetailsUIState>(
        UpcomingMatchDetailsUIState()
    ) {

    init {

        val match = Match(
            id = "",
            groupId = "",
            start = LocalDateTime.parse("2023-01-01T12:00:00"),
            playground = "",
            maxPlayers = 1,
            minPlayers = 2,
            cadre = emptyList(),
            waitingBench = emptyList(),
            deregistered = emptyList(),
            result = emptyList()
        )
        val userId = ""

        val userPosition = when {
            match == null || userId == null -> null
            userId in match.cadre -> PlayerStatus.CADRE
            userId in match.waitingBench -> PlayerStatus.WAITING_BENCH
            userId in match.deregistered -> PlayerStatus.DEREGISTERED
            else -> null
        }

        val startRegistration: Boolean? =
            when (userPosition) {
                PlayerStatus.CADRE -> true
                PlayerStatus.WAITING_BENCH -> true
                PlayerStatus.DEREGISTERED -> false
                else -> null
            }

        uiStateMutable.update {
            it.copy(userPosition = userPosition, startRegistration = startRegistration)
        }
    }

    override fun processIntent(intent: UpcomingMatchDetailsIntent) {
    }
}

data class UpcomingMatchDetailsUIState(
    override val isLoading: Boolean = false,
    override val error: String? = null,
    val userPosition: PlayerStatus? = null,
    val userRole: UserRole? = null,
    val startRegistration: Boolean? = null,
    val selectedRegistration: Boolean? = null,
    val groupNameList: Map<String, String> = emptyMap(),
    val cadre: List<String> = emptyList(),
    val waitingBench: List<String> = emptyList(),
    val deregistered: List<String> = emptyList(),
    val start: LocalDateTime? = null,
    val playground: String? = null,
    val actualPlayersCount: Int = 0,
    val maxPlayers: Int = 0
) : BaseUIState<UpcomingMatchDetailsUIState> {
    override fun copyWith(isLoading: Boolean, error: String?): UpcomingMatchDetailsUIState =
        copy(isLoading = isLoading, error = error)
}

sealed class UpcomingMatchDetailsIntent {
    object CancelMatch : UpcomingMatchDetailsIntent()
    data class SelectRegistration(val registration: Boolean) : UpcomingMatchDetailsIntent()
    data class RegisterPlayer(val registration: Boolean) : UpcomingMatchDetailsIntent()
    data class RemoveFromCadre(val id: String) : UpcomingMatchDetailsIntent()
    data class AddToCadre(val id: String) : UpcomingMatchDetailsIntent()
}

sealed class UpcomingMatchDetailsEffect {
    object MatchCancelled : UpcomingMatchDetailsEffect()
}