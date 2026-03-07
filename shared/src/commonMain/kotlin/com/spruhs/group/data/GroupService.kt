package com.spruhs.group.data

import com.spruhs.group.application.Group
import com.spruhs.group.application.GroupNameEntry
import com.spruhs.group.application.PlayerDetails
import com.spruhs.user.application.UserRole
import com.spruhs.user.application.UserStatus
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import kotlinx.serialization.Serializable

class GroupService(private val groupApi: GroupApi) {

    suspend fun createGroup(name: String): String = groupApi.createGroup(CreateGroupRequest(name))

    suspend fun updateGroupName(groupId: String, name: String) {
        groupApi.updateGroupName(groupId, name)
    }

    suspend fun updatePlayer(
        groupId: String,
        userId: String,
        status: UserStatus? = null,
        role: UserRole? = null
    ) {
        groupApi.updatePlayer(groupId, userId, status?.name, role?.name)
    }

    suspend fun removePlayer(groupId: String, userId: String) {
        groupApi.removePlayer(groupId, userId)
    }

    suspend fun inviteUser(groupId: String, userId: String) {
        groupApi.inviteUser(groupId, userId)
    }

    suspend fun respondToInvitation(groupId: String, userId: String, response: Boolean) {
        groupApi.invitedUserResponse(InviteUserResponseRequest(groupId, userId, response))
    }

    suspend fun getGroup(groupId: String): Group = groupApi.getGroup(groupId).toGroup()

    suspend fun getGroupPlayer(groupId: String, userId: String): PlayerDetails =
        groupApi.getGroupPlayer(groupId, userId).toPlayerDetails()

    suspend fun getGroupNameList(groupId: String): List<GroupNameEntry> =
        groupApi.getGroupNameList(groupId).map { it.toGroupNameEntry() }
}

interface GroupApi {

    @POST("v1/group")
    suspend fun createGroup(@Body request: CreateGroupRequest): String

    @PUT("v1/group/{groupId}/name")
    suspend fun updateGroupName(@Path("groupId") groupId: String, @Query("name") name: String)

    @PUT("v1/group/{groupId}/players/{userId}")
    suspend fun updatePlayer(
        @Path("groupId") groupId: String,
        @Path("userId") userId: String,
        @Query("status") status: String?,
        @Query("role") role: String?
    )

    @DELETE("v1/group/{groupId}/players/{userId}")
    suspend fun removePlayer(@Path("groupId") groupId: String, @Path("userId") userId: String)

    @POST("v1/group/{groupId}/invited-users/{userId}")
    suspend fun inviteUser(@Path("groupId") groupId: String, @Path("userId") userId: String)

    @PUT("v1/group/invited-users")
    suspend fun invitedUserResponse(@Body request: InviteUserResponseRequest)

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

private fun GroupMessage.toGroup() = Group(
    id = groupId,
    name = name,
    players = players.map { it.toPlayerDetails() }
)

private fun GroupPlayerMessage.toPlayerDetails() = PlayerDetails(
    id = userId,
    status = UserStatus.valueOf(status),
    role = UserRole.valueOf(role),
    avatarUrl = avatarUrl,
    email = email
)

private fun GroupNameEntryMessage.toGroupNameEntry() = GroupNameEntry(
    id = userId,
    name = name
)