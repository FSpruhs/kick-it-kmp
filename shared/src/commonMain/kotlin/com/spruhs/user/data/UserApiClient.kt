package com.spruhs.user.data

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import kotlinx.serialization.Serializable

interface UserAuthApiClient {
    @GET("v1/user/{userId}")
    suspend fun getUser(@Path("userId") userId: String): UserMessage

    @PUT("v1/user/{userId}/nickName")
    suspend fun changeNickName(@Path("userId") userId: String, @Query("nickName") nickName: String)
}

interface UserNoAuthApiClient {
    @POST("v1/user")
    suspend fun registerUser(@Body request: RegisterUserRequest): String
}

@Serializable
data class UserMessage(
    val id: String,
    val nickName: String,
    val email: String,
    val imageId: String? = null,
    val groups: List<GroupInfoMessage> = emptyList()
)

@Serializable
data class GroupInfoMessage(
    val id: String,
    val name: String,
    val userStatus: String,
    val userRole: String,
    val lastMatch: String? = null
)

@Serializable
data class RegisterUserRequest(val nickName: String, val email: String, val password: String?)