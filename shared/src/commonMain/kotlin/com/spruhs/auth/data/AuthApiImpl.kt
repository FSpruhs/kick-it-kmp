package com.spruhs.auth.data

import com.spruhs.auth.application.AuthApi
import com.spruhs.auth.application.AuthTokens
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import kotlinx.serialization.Serializable

class AuthApiImpl(private val service: AuthService) : AuthApi {
    override suspend fun login(email: String, password: String): AuthTokens? {
        val response = service.login(LoginRequest(email, password))
        return AuthTokens(response.accessToken, response.refreshToken)
    }

    override suspend fun refreshToken(refreshToken: String): AuthTokens {
        val response = service.refreshToken(refreshToken)
        return AuthTokens(response.accessToken, response.refreshToken)
    }
}

interface AuthService {
    @POST("v1/auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("v1/auth/refresh/{refreshToken}")
    suspend fun refreshToken(@Path("refreshToken") refreshToken: String): AuthResponse
}

@Serializable
data class AuthResponse(val accessToken: String, val refreshToken: String)

@Serializable
data class LoginRequest(val email: String, val password: String)