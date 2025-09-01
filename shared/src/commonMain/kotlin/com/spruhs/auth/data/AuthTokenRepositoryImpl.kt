package com.spruhs.auth.data

import com.spruhs.auth.application.AuthApi
import com.spruhs.auth.application.AuthToken
import com.spruhs.auth.application.AuthTokenRepository
import kotlin.concurrent.Volatile

class AuthTokenRepositoryImpl(
    private val authTokenDao: AuthTokenDao,
    private val authApi: AuthApi
) : AuthTokenRepository {
    @Volatile
    private var cachedToken: AuthToken? = null

    override suspend fun saveToken(accessToken: String, refreshToken: String) {
        val entity = AuthTokenEntity(accessToken = accessToken, refreshToken = refreshToken)
        authTokenDao.saveToken(entity)
        cachedToken = entity.toToken()
    }

    override suspend fun deleteToken() {
        authTokenDao.deleteToken()
        cachedToken = null
    }

    override suspend fun getToken(): AuthToken? {
        if (cachedToken == null) {
            cachedToken = authTokenDao.getToken()?.toToken()
        }
        return cachedToken
    }

    override suspend fun refreshToken(refreshToken: String): Pair<String, String> =
        authApi.refreshToken(refreshToken)

    fun getTokenSync(): AuthToken? = cachedToken

    private fun AuthTokenEntity.toToken() = AuthToken(accessToken, refreshToken)
}