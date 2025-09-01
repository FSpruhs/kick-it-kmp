package com.spruhs.auth.application

class LoginUseCase(
    private val authApi: AuthApi,
    private val tokenHelper: TokenHelper,
    private val authTokenRepository: AuthTokenRepository
) {
    suspend fun login(email: String, password: String): String? = authApi.login(email, password)
        ?.also { authTokenRepository.saveToken(it) }
        ?.getUserId()

    private fun AuthTokens.getUserId(): String = tokenHelper.getUserId(accessToken)
}