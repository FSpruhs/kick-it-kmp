package com.spruhs.auth.application

import com.spruhs.AppLogger

class AuthenticateUseCase(
    private val authTokenRepository: AuthTokenRepository,
    private val tokenHelper: TokenHelper
) {
    suspend fun authenticate(): String? =
        getValidToken()?.getUserId()

    private fun AuthTokens.getUserId(): String = tokenHelper.getUserId(accessToken)

    private suspend fun getValidToken(): AuthTokens? {
        val tokens = authTokenRepository.getToken()?.also {
            AppLogger.i("AuthViewModel", "Token found")
        } ?: run {
            AppLogger.i("AuthViewModel", "No token found")
            return null
        }

        return when {
            tokenHelper.isTokenValid(tokens.accessToken) -> {
                AppLogger.i("AuthViewModel", "Access token is valid")
                tokens
            }
            tokenHelper.isTokenValid(tokens.refreshToken) -> {
                AppLogger.i("AuthViewModel", "Refresh token is valid")
                tokens.refreshTokens()
            }
            else -> {
                AppLogger.d("AuthViewModel", "No valid token found")
                null
            }
        }
    }

    private suspend fun AuthTokens.refreshTokens(): AuthTokens {
        val tokens = authTokenRepository.refreshToken(refreshToken)
        authTokenRepository.saveToken(tokens)
        return AuthTokens(accessToken, refreshToken)
    }
}