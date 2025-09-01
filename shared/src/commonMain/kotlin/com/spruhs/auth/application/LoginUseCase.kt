package com.spruhs.auth.application

class LoginUseCase(
    private val authApi: AuthApi,
    private val tokenHelper: TokenHelper,
    private val authTokenRepository: AuthTokenRepository
) {
    suspend fun login(email: String, password: String): String? {
        val (accessToken, refreshToken) = authApi.login(email, password)
        if (accessToken != null && refreshToken != null) {
            authTokenRepository.saveToken(accessToken, refreshToken)
            return tokenHelper.getUserId(accessToken)
        }
        return null
    }
}