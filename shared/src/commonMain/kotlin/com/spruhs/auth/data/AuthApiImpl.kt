package com.spruhs.auth.data

import com.spruhs.auth.application.AuthApi
import com.spruhs.auth.application.AuthTokens
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.contentType

import kotlinx.serialization.Serializable

class AuthApiImpl(private val service: AuthService, private val client: HttpClient) : AuthApi {
    override suspend fun login(email: String, password: String): AuthTokens? {
        //val response = client.post("http://10.0.2.2:8085/api/v1/auth/login") {
        //    contentType(io.ktor.http.ContentType.Application.Json)
        //    setBody(LoginRequest("fabian@spruhs.com", "Password123"))
        //    }
        //val authResponse: AuthResponse = response.body()
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