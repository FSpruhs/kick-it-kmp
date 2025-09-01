package com.spruhs.auth.application

import com.spruhs.AppLogger

class AuthenticateUseCase(
    private val authTokenRepository: AuthTokenRepository,
    private val tokenHelper: TokenHelper
) {
    private suspend fun getValidToken(): AuthToken? {
        val tokens = authTokenRepository.getToken()
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

    suspend fun authenticate(): String? {
        val validToken = getValidToken()
        if (validToken == null) {
            return null
        }

        return tokenHelper.getUserId(validToken.accessToken)
    }

    private suspend fun refreshToken(refresherToken: String): AuthToken {
        val (accessToken, refreshToken) = authTokenRepository.refreshToken(refresherToken)
        authTokenRepository.saveToken(accessToken, refreshToken)
        return AuthToken(accessToken, refreshToken)
    }
}