package com.spruhs.auth.presentation

import com.spruhs.AppLogger
import com.spruhs.BaseViewModel
import com.spruhs.auth.application.AuthenticateUseCase
import com.spruhs.user.application.LoadUserUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

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
        scope.launch {
            delay(2000)
            try {
                AppLogger.i("StartViewModel", "Authenticating")
                val userId = authenticateUseCase.authenticate()
                if (userId == null) {
                    _effects.emit(StartSideEffect.NotAuthenticated)
                } else {
                    loadUserUseCase.loadUser(userId)
                    _effects.emit(StartSideEffect.Authenticated)
                }
            } catch (e: RuntimeException) {
                AppLogger.e("StartViewModel", "Authentication failed: ${e.message}", e)
                _effects.emit(StartSideEffect.NotAuthenticated)
            }
        }
    }
}

sealed class StartSideEffect {
    object Authenticated : StartSideEffect()
    object NotAuthenticated : StartSideEffect()
}