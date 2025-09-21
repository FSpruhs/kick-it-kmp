package com.spruhs.auth.presentation

import com.spruhs.BaseUIState
import com.spruhs.BaseViewModel
import com.spruhs.auth.application.AuthenticateUseCase
import com.spruhs.user.application.LoadUserUseCase

class StartViewModel(
    private val authenticateUseCase: AuthenticateUseCase,
    private val loadUserUseCase: LoadUserUseCase
) : BaseViewModel<StartSideEffect, StartUiState>(StartUiState()) {

    init {
        authenticate()
    }

    private fun authenticate() {
        performAction(
            onSuccess = { onAuthenticated(it) },
            onError = { effectsMutable.emit(StartSideEffect.NotAuthenticated) },
            action = { authenticateUseCase.authenticate() }
        )
    }

    private suspend fun onAuthenticated(userId: String?) {
        if (userId == null) {
            effectsMutable.emit(StartSideEffect.NotAuthenticated)
        } else {
            loadUserUseCase.loadUser(userId)
            effectsMutable.emit(StartSideEffect.Authenticated)
        }
    }
}

sealed class StartSideEffect {
    object Authenticated : StartSideEffect()
    object NotAuthenticated : StartSideEffect()
}

data class StartUiState(
    override val isLoading: Boolean = false,
    override val error: String? = null,
    ) : BaseUIState<StartUiState> {
    override fun copyWith(isLoading: Boolean): StartUiState = copy(isLoading = isLoading)
}