package com.spruhs.auth.presentation

import com.spruhs.BaseViewModel
import com.spruhs.auth.application.AuthenticateUseCase
import com.spruhs.user.application.LoadUserUseCase
import kotlinx.coroutines.flow.update

class StartViewModel(
    private val authenticateUseCase: AuthenticateUseCase,
    private val loadUserUseCase: LoadUserUseCase
) : BaseViewModel<StartSideEffect, StartUiState>(StartUiState()) {

    init {
        authenticate()
    }

    private fun authenticate() {
        performAction(
            setLoading = { isLoading -> uiStateMutable.update { it.copy(isLoading = isLoading) } },
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
    val isLoading: Boolean = false
)