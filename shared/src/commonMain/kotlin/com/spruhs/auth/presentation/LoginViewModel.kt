package com.spruhs.auth.presentation

import com.spruhs.BaseViewModel
import com.spruhs.auth.application.LoginUseCase
import com.spruhs.user.application.LoadUserUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val loadUserUseCase: LoadUserUseCase
) : BaseViewModel() {
    private val _loginUIState = MutableStateFlow(LoginUIState())
    val loginUIState = _loginUIState.asStateFlow()

    private val _effects = MutableSharedFlow<LoginSideEffect>()
    val effects = _effects.asSharedFlow()

    fun processIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.Login -> login()
            is LoginIntent.Register -> scope.launch { _effects.emit(LoginSideEffect.Register) }
            is LoginIntent.EmailChanged -> updateLoginState(email = intent.email)
            is LoginIntent.PasswordChanged -> updateLoginState(password = intent.password)
        }
    }

    private fun updateLoginState(
        email: String = _loginUIState.value.email,
        password: String = _loginUIState.value.password
    ) {
        _loginUIState.value = _loginUIState.value.copy(
            email = email,
            password = password,
            isInputValid = validateInput(email, password)
        )
    }

    private fun validateInput(email: String, password: String): Boolean {
        val isEmailValid = email.contains("@") && email.contains(".") && email.length > 5
        val isPasswordValid = password.length >= 8
        return isEmailValid && isPasswordValid
    }

    private fun login() {
        performAction(
            setLoading = { isLoading ->_loginUIState.update { it.copy(isLoading = isLoading) }},
            onSuccess = { onLoginSuccess(it) },
            onError = { _loginUIState.update { it.copy(loginError = true) } },
            action = {
                require(loginUIState.value.isInputValid)
                loginUseCase.login(_loginUIState.value.email, _loginUIState.value.password)
            }
        )
    }

    private suspend fun onLoginSuccess(userId: String?) {
        if (userId != null) {
            loadUserUseCase.loadUser(userId)
            _effects.emit(LoginSideEffect.LoginSuccess)
        } else {
            _loginUIState.update { it.copy(loginError = true) }
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
    val loginError: Boolean = false,
    val isLoading: Boolean = false
)