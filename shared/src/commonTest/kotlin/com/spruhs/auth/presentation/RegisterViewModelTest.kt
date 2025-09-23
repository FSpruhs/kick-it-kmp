package com.spruhs.auth.presentation

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.spruhs.MainDispatcherTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest : MainDispatcherTest() {

    @Test
    fun `email changed updates state`() = runTest {
        // Given
        val viewModel = RegisterViewModel()
        val testMail = "test@mail.com"

        // When
        viewModel.processIntent(RegisterIntent.EmailChanged(testMail))

        // Then
        assertThat(testMail).isEqualTo(viewModel.uiState.value.email)
    }

    @Test
    fun `nickname longer than max is ignored`() = runTest {
        // Given
        val viewModel = RegisterViewModel()
        val tooLongNick = "a".repeat(25)

        // When
        viewModel.processIntent(RegisterIntent.NickNameChanged(tooLongNick))

        // Then
        assertThat(viewModel.uiState.value.nickName).isEmpty()
    }

    @Test
    fun `password changed updates state`() = runTest {
        // Given
        val viewModel = RegisterViewModel()
        val password = "Secret123"

        // When
        viewModel.processIntent(RegisterIntent.PasswordChanged(password))

        // Then
        assertThat(password).isEqualTo(viewModel.uiState.value.password)
    }

    @Test
    fun `valid input makes state valid`() = runTest {
        // Given
        val viewModel = RegisterViewModel()
        val email = "test@mail.com"
        val nickName = "test"
        val password = "Secret123"

        // When
        viewModel.processIntent(RegisterIntent.EmailChanged(email))
        viewModel.processIntent(RegisterIntent.NickNameChanged(nickName))
        viewModel.processIntent(RegisterIntent.PasswordChanged(password))
        viewModel.processIntent(RegisterIntent.RepeatedPasswordChanged(password))

        // Then
        assertThat(viewModel.uiState.value.isInputValid).isTrue()
    }

    @Test
    fun `repeated password mismatch makes input invalid`() = runTest {
        // Given
        val viewModel = RegisterViewModel()
        val email = "test@mail.com"
        val nickName = "test"
        val password = "Secret123"

        // When
        viewModel.processIntent(RegisterIntent.EmailChanged(email))
        viewModel.processIntent(RegisterIntent.NickNameChanged(nickName))
        viewModel.processIntent(RegisterIntent.PasswordChanged(password))
        viewModel.processIntent(RegisterIntent.RepeatedPasswordChanged(password))
        viewModel.processIntent(RegisterIntent.RepeatedPasswordChanged("Other123"))

        // Then
        assertThat(viewModel.uiState.value.isInputValid).isFalse()
    }

    @Test
    fun `invalid email makes input invalid`() = runTest {
        // Given
        val viewModel = RegisterViewModel()
        val email = "test@mail"
        val nickName = "test"
        val password = "Secret123"

        // When
        viewModel.processIntent(RegisterIntent.EmailChanged(email))
        viewModel.processIntent(RegisterIntent.NickNameChanged(nickName))
        viewModel.processIntent(RegisterIntent.PasswordChanged(password))
        viewModel.processIntent(RegisterIntent.RepeatedPasswordChanged(password))
        viewModel.processIntent(RegisterIntent.RepeatedPasswordChanged(password))

        // Then
        assertThat(viewModel.uiState.value.isInputValid).isFalse()
    }

    @Test
    fun `invalid nickname makes input invalid`() = runTest {
        // Given
        val viewModel = RegisterViewModel()
        val email = "test@mail.com"
        val nickName = ""
        val password = "Secret123"

        // When
        viewModel.processIntent(RegisterIntent.EmailChanged(email))
        viewModel.processIntent(RegisterIntent.NickNameChanged(nickName))
        viewModel.processIntent(RegisterIntent.PasswordChanged(password))
        viewModel.processIntent(RegisterIntent.RepeatedPasswordChanged(password))
        viewModel.processIntent(RegisterIntent.RepeatedPasswordChanged(password))

        // Then
        assertThat(viewModel.uiState.value.isInputValid).isFalse()
    }
}
