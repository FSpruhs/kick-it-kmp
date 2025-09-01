package com.spruhs.auth.presentation

import com.spruhs.AppLogger
import com.spruhs.BaseViewModel
import com.spruhs.auth.application.AuthenticateUseCase
import com.spruhs.user.application.LoadUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StartViewModel(
    private val authenticateUseCase: AuthenticateUseCase,
    private val loadUserUseCase: LoadUserUseCase
) : BaseViewModel() {
    private val _startUIState = MutableStateFlow(StartUIState())
    val startUIState: StateFlow<StartUIState> = _startUIState.asStateFlow()

    init {
        authenticate()
    }

    private fun authenticate() {
        scope.launch {
            try {
                AppLogger.i("StartViewModel", "Authenticating")
                val userId = authenticateUseCase.authenticate()
                if (userId == null) {
                    _startUIState.value = _startUIState.value.copy(authenticated = false)
                } else {
                    loadUserUseCase.loadUser(userId)
                    _startUIState.value = _startUIState.value.copy(authenticated = true)
                }
            } catch (e: RuntimeException) {
                AppLogger.e("StartViewModel", "Authentication failed: ${e.message}", e)
                _startUIState.value = _startUIState.value.copy(authenticated = false)
            }
        }
    }
}

data class StartUIState(val authenticated: Boolean? = null)