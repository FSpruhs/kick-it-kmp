package com.spruhs.group.data

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import io.ktor.client.statement.HttpResponse
import kotlinx.serialization.Serializable

interface GroupApiClient {

    @POST("v1/group")
    suspend fun createGroup(@Body request: CreateGroupRequest): String

    @PUT("v1/group/{groupId}/name")
    suspend fun updateGroupName(@Path("groupId") groupId: String, @Query("name") name: String): HttpResponse

    @PUT("v1/group/{groupId}/players/{userId}")
    suspend fun updatePlayer(
        @Path("groupId") groupId: String,
        @Path("userId") userId: String,
        @Query("status") status: String?,
        @Query("role") role: String?
    ): HttpResponse

    @DELETE("v1/group/{groupId}/players/{userId}")
    suspend fun removePlayer(@Path("groupId") groupId: String, @Path("userId") userId: String): HttpResponse

    @POST("v1/group/{groupId}/invited-users/{userId}")
    suspend fun inviteUser(@Path("groupId") groupId: String, @Path("userId") userId: String): HttpResponse

    @PUT("v1/group/invited-users")
    suspend fun invitedUserResponse(@Body request: InviteUserResponseRequest): HttpResponse

    @GET("v1/group/{groupId}")
    suspend fun getGroup(@Path("groupId") groupId: String): GroupMessage

    @GET("v1/group/{groupId}/player/{userId}")
    suspend fun getGroupPlayer(
        @Path("groupId") groupId: String,
        @Path("userId") userId: String
    ): GroupPlayerMessage

    @GET("v1/group/{groupId}/name-list")
    suspend fun getGroupNameList(@Path("groupId") groupId: String): List<GroupNameEntryMessage>
}

@Serializable
data class CreateGroupRequest(val name: String)

@Serializable
data class InviteUserResponseRequest(val groupId: String, val userId: String, val response: Boolean)

@Serializable
data class GroupMessage(
    val groupId: String,
    val name: String,
    val players: List<GroupPlayerMessage>
)

@Serializable
data class GroupPlayerMessage(
    val userId: String,
    val role: String,
    val status: String,
    val avatarUrl: String? = null,
    val email: String
)

@Serializable
data class GroupNameEntryMessage(val userId: String, val name: String)