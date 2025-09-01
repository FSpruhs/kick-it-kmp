package com.spruhs.auth.application

import com.spruhs.AppLogger

class AuthenticateUseCase(
    private val tokenRepository: TokenRepository,
    private val authRepository: AuthenticationRepository,
    private val userRepository: UserRepository,
    private val tokenHelper: TokenHelper
) {
    private suspend fun getValidToken(): AuthToken? {
        val tokens = tokenRepository.getToken()
        if (tokens == null) {
            AppLogger.i("AuthViewModel", "No token found")
            return null
        }

        val isAccessValid = tokenHelper.isTokenValid(tokens.accessToken)
        if (isAccessValid) {
            AppLogger.i("AuthViewModel", "Access token is valid")
            return tokens
        }

        val isRefreshValid = tokenHelper.isTokenValid(tokens.refreshToken)
        if (isRefreshValid) {
            AppLogger.i("AuthViewModel", "Refresh token is valid")
            return refreshToken(tokens.refreshToken)
        }

        AppLogger.d("AuthViewModel", "No valid token found")
        return null
    }

    suspend fun authenticate(): Boolean {
        val validToken = getValidToken()
        if (validToken == null) {
            return false
        }

        val userId = tokenHelper.getUserId(validToken.accessToken)
        userRepository.loadUser(userId)

        return true
    }

    private suspend fun refreshToken(refresherToken: String): AuthToken {
        val (accessToken, refreshToken) = authRepository.refreshToken(refresherToken)
        tokenRepository.saveToken(accessToken, refreshToken)
        return AuthToken(accessToken, refreshToken)
    }
}