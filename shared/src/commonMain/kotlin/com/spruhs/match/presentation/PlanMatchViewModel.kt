package com.spruhs.match.presentation

import com.spruhs.BaseUIState
import com.spruhs.BaseViewModel
import kotlinx.datetime.LocalDateTime

class PlanMatchViewModel : BaseViewModel<PlanMatchEffect, PlanMatchUIState>(PlanMatchUIState()) {
    fun processIntent(intent: PlanMatchIntent) {
    }
}

data class PlanMatchUIState(
    override val isLoading: Boolean = false,
    override val error: String? = null,
    val start: LocalDateTime? = null,
    val location: String = "",
    val minPlayers: Int = 4,
    val maxPlayers: Int = 8
) : BaseUIState<PlanMatchUIState> {
    override fun copyWith(isLoading: Boolean, error: String?): PlanMatchUIState =
        copy(isLoading = isLoading, error = error)
}

sealed class PlanMatchEffect {
    object MatchPlanned : PlanMatchEffect()
}

sealed class PlanMatchIntent {
    object PlanMatch : PlanMatchIntent()
    data class SelectDateTime(val dateTime: LocalDateTime) : PlanMatchIntent()
    data class SelectLocation(val location: String) : PlanMatchIntent()
    data class SelectMinPlayers(val minPlayers: Int) : PlanMatchIntent()
    data class SelectMaxPlayers(val maxPlayers: Int) : PlanMatchIntent()
}