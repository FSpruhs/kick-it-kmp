package com.spruhs.auth.presentation

import androidx.lifecycle.viewModelScope
import com.spruhs.BaseUIState
import com.spruhs.BaseViewModel
import com.spruhs.auth.application.LoginUseCase
import com.spruhs.user.application.LoadUserUseCase
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val loadUserUseCase: LoadUserUseCase
) : BaseViewModel<LoginSideEffect, LoginUIState>(LoginUIState()) {

    fun processIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.Login -> login()
            is LoginIntent.Register -> viewModelScope.launch {
                effectsMutable.emit(LoginSideEffect.Register)
            }
            is LoginIntent.EmailChanged -> updateLoginState(email = intent.email)
            is LoginIntent.PasswordChanged -> updateLoginState(password = intent.password)
        }
    }

    private fun updateLoginState(
        email: String = uiStateMutable.value.email,
        password: String = uiStateMutable.value.password
    ) {
        uiStateMutable.value = uiStateMutable.value.copy(
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
            onSuccess = { onLoginSuccess(it) },
            onError = { uiStateMutable.update { it.copy(loginError = true) } },
            action = {
                require(uiStateMutable.value.isInputValid)
                loginUseCase.login(uiStateMutable.value.email, uiStateMutable.value.password)
            }
        )
    }

    private suspend fun onLoginSuccess(userId: String?) {
        if (userId != null) {
            loadUserUseCase.loadUser(userId)
            effectsMutable.emit(LoginSideEffect.LoginSuccess)
        } else {
            uiStateMutable.update { it.copy(loginError = true) }
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
    override val isLoading: Boolean = false,
) : BaseUIState<LoginUIState> {
    override fun copyWith(isLoading: Boolean): LoginUIState { return copy(isLoading = isLoading) }
}