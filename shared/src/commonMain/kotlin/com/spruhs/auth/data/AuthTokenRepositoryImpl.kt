package com.spruhs.auth.data

import com.spruhs.auth.application.AuthApi
import com.spruhs.auth.application.AuthTokens
import com.spruhs.auth.application.AuthTokenRepository
import kotlin.concurrent.Volatile

class AuthTokenRepositoryImpl(
    private val authTokenDao: AuthTokenDao,
    private val authApi: AuthApi
) : AuthTokenRepository {
    @Volatile
    private var cachedToken: AuthTokens? = null

    override suspend fun saveToken(authTokens: AuthTokens) {
        authTokenDao.saveToken(authTokens.toEntity())
        cachedToken = authTokens
    }

    override suspend fun deleteToken() {
        authTokenDao.deleteToken()
        cachedToken = null
    }

    override suspend fun getToken(): AuthTokens? = cachedToken
            ?: authTokenDao
                .getToken()
                ?.toToken()
                ?.also { cachedToken = it }

    override suspend fun refreshToken(refreshToken: String): AuthTokens =
        authApi.refreshToken(refreshToken)

    fun getTokenSync(): AuthTokens? = cachedToken

    private fun AuthTokenEntity.toToken() = AuthTokens(accessToken, refreshToken)

    private fun AuthTokens.toEntity() = AuthTokenEntity(accessToken = accessToken, refreshToken = refreshToken)
}