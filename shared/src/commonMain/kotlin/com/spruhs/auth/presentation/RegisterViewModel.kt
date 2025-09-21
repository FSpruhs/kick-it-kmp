package com.spruhs.auth.presentation

import androidx.lifecycle.viewModelScope
import com.spruhs.AppLogger
import com.spruhs.BaseUIState
import com.spruhs.BaseViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel : BaseViewModel<RegisterEffect, RegisterUIState>(RegisterUIState()) {

    companion object {
        private const val MAX_NICKNAME_LENGTH = 20
    }

    fun processIntent(intent: RegisterIntent) {
        when (intent) {
            is RegisterIntent.Register -> register()
            is RegisterIntent.Back -> viewModelScope.launch {
                effectsMutable.emit(RegisterEffect.Back)
            }
            is RegisterIntent.EmailChanged -> updateRegisterState(email = intent.email)
            is RegisterIntent.NickNameChanged -> updateRegisterState(nickName = intent.nickName)
            is RegisterIntent.PasswordChanged -> updateRegisterState(password = intent.password)
            is RegisterIntent.RepeatedPasswordChanged -> updateRegisterState(
                repeatedPassword = intent.repeatedPassword
            )
        }
    }

    private fun updateRegisterState(
        email: String = uiStateMutable.value.email,
        nickName: String = uiStateMutable.value.nickName,
        password: String = uiStateMutable.value.password,
        repeatedPassword: String = uiStateMutable.value.repeatedPassword
    ) {
        if (nickName.length > MAX_NICKNAME_LENGTH) return
        uiStateMutable.value = uiStateMutable.value.copy(
            email = email,
            nickName = nickName,
            password = password,
            repeatedPassword = repeatedPassword,
            isInputValid = validateInput(email, nickName, password, repeatedPassword)
        )
    }

    private fun validateInput(
        email: String,
        nickName: String,
        password: String,
        repeatedPassword: String
    ): Boolean = email.contains("@") &&
        email.contains(".") &&
        email.length > 5 &&
        nickName.length >= 2 &&
        nickName.length <= MAX_NICKNAME_LENGTH &&
        password.length >= 6 &&
        password == repeatedPassword

    private fun validatePassword(password: String) = try {
        require(password.length >= 6)
        require(password.any { it.isUpperCase() })
        require(password.any { it.isLowerCase() })
        require(password.any { it.isDigit() })
        true
    } catch (e: IllegalArgumentException) {
        AppLogger.e("RegisterScreen", "validatePassword: ${e.message}")
        false
    }

    private fun register() {
        if (!validatePassword(uiStateMutable.value.password)) {
            uiStateMutable.update { it.copy(isPasswordValid = false) }
            return
        }
        performAction(
            setLoading = { isLoading -> uiStateMutable.update { it.copy(isLoading = isLoading) } },
            onSuccess = {
                viewModelScope.launch { effectsMutable.emit(RegisterEffect.RegisterSuccess) }
            },
            onError = { uiStateMutable.update { it.copy(isPasswordValid = false) } },
            action = {
                uiStateMutable.update { it.copy(isPasswordValid = validatePassword(it.password)) }
            }
        )
    }
}

sealed class RegisterEffect {
    object RegisterSuccess : RegisterEffect()
    object Back : RegisterEffect()
}

sealed class RegisterIntent {
    object Register : RegisterIntent()
    object Back : RegisterIntent()
    data class EmailChanged(val email: String) : RegisterIntent()
    data class NickNameChanged(val nickName: String) : RegisterIntent()
    data class PasswordChanged(val password: String) : RegisterIntent()
    data class RepeatedPasswordChanged(val repeatedPassword: String) : RegisterIntent()
}

data class RegisterUIState(
    val email: String = "",
    val nickName: String = "",
    val password: String = "",
    val repeatedPassword: String = "",
    val isPasswordValid: Boolean? = null,
    val isInputValid: Boolean = false,
    override val isLoading: Boolean = false,
    override val error: String? = null
) : BaseUIState