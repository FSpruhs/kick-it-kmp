package com.spruhs.auth.presentation

import com.spruhs.BaseViewModel
import com.spruhs.auth.application.AuthenticateUseCase
import com.spruhs.user.application.LoadUserUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class StartViewModel(
    private val authenticateUseCase: AuthenticateUseCase,
    private val loadUserUseCase: LoadUserUseCase
) : BaseViewModel() {
    private val _effects = MutableSharedFlow<StartSideEffect>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val effects = _effects.asSharedFlow()

    init {
        authenticate()
    }

    private fun authenticate() {
        performAction(
            onSuccess = { onAuthenticated(it) },
            onError = { _effects.emit(StartSideEffect.NotAuthenticated) },
            action = { authenticateUseCase.authenticate() }
        )
    }

    private suspend fun onAuthenticated(userId: String?) {
        if (userId == null) {
            _effects.emit(StartSideEffect.NotAuthenticated)
        } else {
            loadUserUseCase.loadUser(userId)
            _effects.emit(StartSideEffect.Authenticated)
        }
    }
}

sealed class StartSideEffect {
    object Authenticated : StartSideEffect()
    object NotAuthenticated : StartSideEffect()
}