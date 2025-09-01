package com.spruhs.auth.presentation

import com.spruhs.AppLogger
import com.spruhs.BaseViewModel
import com.spruhs.auth.application.LoginUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val loginUseCase: LoginUseCase) : BaseViewModel() {
    private val _loginUIState = MutableStateFlow(LoginUIState())
    val loginUIState = _loginUIState.asStateFlow()

    private val _effects = MutableSharedFlow<LoginSideEffect>()
    val effects = _effects.asSharedFlow()

    fun processIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.Login -> login()
            is LoginIntent.Register -> scope.launch { _effects.emit(LoginSideEffect.Register) }
            is LoginIntent.EmailChanged -> handleEmailChanged(intent.email)
            is LoginIntent.PasswordChanged -> handlePasswordChanged(intent.password)
        }
    }

    private fun handleEmailChanged(email: String) {
        _loginUIState.value =
            _loginUIState.value.copy(
                email = email,
                isInputValid = validateInput(email, _loginUIState.value.password)
            )
    }

    private fun handlePasswordChanged(password: String) {
        _loginUIState.value =
            _loginUIState.value.copy(
                password = password,
                isInputValid = validateInput(_loginUIState.value.email, password)
            )
    }

    private fun validateInput(email: String, password: String): Boolean {
        val isEmailValid = email.contains("@") && email.contains(".") && email.length > 5
        val isPasswordValid = password.length >= 8
        return isEmailValid && isPasswordValid
    }

    private fun login() {
        scope.launch {
            _loginUIState.value = _loginUIState.value.copy(isLoading = true)
            try {
                val success = loginUseCase.login(
                    _loginUIState.value.email,
                    _loginUIState.value.password
                )
                if (success) {
                    _effects.emit(LoginSideEffect.LoginSuccess)
                } else {
                    _loginUIState.value = _loginUIState.value.copy(loginError = true)
                }
            } catch (e: RuntimeException) {
                AppLogger.e("LoginViewModel", "Login failed: ${e.message}", e)
                _loginUIState.value = _loginUIState.value.copy(loginError = true)
            }
        }
    }
}

sealed class LoginIntent {
    object Login : LoginIntent()
    object Register : LoginIntent()
    data class EmailChanged(val email: String) : LoginIntent()
    data class PasswordChanged(val password: String) : LoginIntent()
}

sealed class LoginSideEffect {
    object LoginSuccess : LoginSideEffect()
    object Register : LoginSideEffect()
}

data class LoginUIState(
    val email: String = "",
    val password: String = "",
    val isInputValid: Boolean = false,
    val loadingUserError: String? = null,
    val loginError: Boolean = false,
    val isLoading: Boolean = false
)