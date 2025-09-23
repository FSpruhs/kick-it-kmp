package com.spruhs.auth.presentation

import com.spruhs.BaseUIState
import com.spruhs.BaseViewModel
import com.spruhs.auth.application.AuthenticateUseCase
import com.spruhs.user.application.LoadUserUseCase

class StartViewModel(
    private val authenticateUseCase: AuthenticateUseCase,
    private val loadUserUseCase: LoadUserUseCase
) : BaseViewModel<StartIntent, StartEffect, StartUiState>(StartUiState()) {

    init {
        authenticate()
    }

    private fun authenticate() {
        performAction(
            onSuccess = { onAuthenticated(it) },
            onError = { effectsMutable.emit(StartEffect.NotAuthenticated) },
            action = { authenticateUseCase.authenticate() }
        )
    }

    private suspend fun onAuthenticated(userId: String?) {
        if (userId == null) {
            effectsMutable.emit(StartEffect.NotAuthenticated)
        } else {
            loadUserUseCase.loadUser(userId)
            effectsMutable.emit(StartEffect.Authenticated)
        }
    }
}

sealed class StartIntent

sealed class StartEffect {
    object Authenticated : StartEffect()
    object NotAuthenticated : StartEffect()
}

data class StartUiState(
    override val isLoading: Boolean = false,
    override val error: String? = null
) : BaseUIState<StartUiState> {
    override fun copyWith(isLoading: Boolean, error: String?): StartUiState =
        copy(isLoading = isLoading, error = error)
}