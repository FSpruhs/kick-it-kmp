package com.spruhs.auth.data

import com.spruhs.auth.application.AuthToken
import com.spruhs.auth.application.AuthTokenRepository
import kotlin.concurrent.Volatile

class AuthTokenRepositoryImpl(private val authTokenDao: AuthTokenDao) : AuthTokenRepository {
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

    fun getTokenSync(): AuthToken? = cachedToken

    private fun AuthTokenEntity.toToken() = AuthToken(accessToken, refreshToken)
}