package com.spruhs.match.presentation

import com.spruhs.BaseUIState
import com.spruhs.BaseViewModel
import com.spruhs.match.application.PlanMatchUseCase
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDateTime

class PlanMatchViewModel(private val planMatchUseCase: PlanMatchUseCase) :
    BaseViewModel<PlanMatchIntent, PlanMatchEffect, PlanMatchUIState>(PlanMatchUIState()) {
    override fun processIntent(intent: PlanMatchIntent) {
        when (intent) {
            is PlanMatchIntent.PlanMatch -> handlePlanMatch()
            is PlanMatchIntent.SelectDateTime -> uiStateMutable.update {
                it.copy(start = intent.dateTime)
            }
            is PlanMatchIntent.SelectLocation -> handleSelectedLocation(intent)
            is PlanMatchIntent.SelectMaxPlayers -> handleSelectedMaxPlayers(intent)
            is PlanMatchIntent.SelectMinPlayers -> handleSelectedMinPlayers(intent)
        }
    }

    private fun handleSelectedLocation(intent: PlanMatchIntent.SelectLocation) {
        val newLocation = intent.location.trim()
        if (newLocation.length > 50) return
        if (newLocation != uiState.value.location) {
            uiStateMutable.update { it.copy(location = newLocation) }
        }
    }

    private fun handleSelectedMinPlayers(intent: PlanMatchIntent.SelectMinPlayers) {
        val newMinPlayers = computeNewPlayerValue(intent.minPlayers, uiState.value.minPlayers)
        if (newMinPlayers != uiState.value.minPlayers) {
            uiStateMutable.update { it.copy(minPlayers = newMinPlayers) }
        }
    }

    private fun computeNewPlayerValue(newValue: String, oldValue: String): String {
        if (newValue.isEmpty()) return ""

        val parsedValue = newValue.toIntOrNull() ?: return oldValue
        if (parsedValue < 0) return oldValue

        return if (parsedValue == 0) "" else newValue
    }

    private fun handleSelectedMaxPlayers(intent: PlanMatchIntent.SelectMaxPlayers) {
        val newMaxPlayers = computeNewPlayerValue(intent.maxPlayers, uiState.value.maxPlayers)
        if (newMaxPlayers != uiState.value.maxPlayers) {
            uiStateMutable.update { it.copy(maxPlayers = newMaxPlayers) }
        }
    }

    private fun handlePlanMatch() {
        performAction(
            onSuccess = { effectsMutable.emit(PlanMatchEffect.MatchPlanned) },
            action = {
                planMatchUseCase.plan(
                    start =
                    uiState.value.start
                        ?: throw IllegalArgumentException("Start date/time must be selected"),
                    location = uiState.value.location,
                    minPlayers =
                    uiState.value.minPlayers.toIntOrNull()
                        ?: throw IllegalArgumentException("Invalid minPlayers value"),
                    maxPlayers =
                    uiState.value.maxPlayers.toIntOrNull()
                        ?: throw IllegalArgumentException("Invalid maxPlayers value")
                )
            }
        )
    }
}

data class PlanMatchUIState(
    override val isLoading: Boolean = false,
    override val error: String? = null,
    val start: LocalDateTime? = null,
    val location: String = "",
    val minPlayers: String = "4",
    val maxPlayers: String = "8"
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
    data class SelectMinPlayers(val minPlayers: String) : PlanMatchIntent()
    data class SelectMaxPlayers(val maxPlayers: String) : PlanMatchIntent()
}