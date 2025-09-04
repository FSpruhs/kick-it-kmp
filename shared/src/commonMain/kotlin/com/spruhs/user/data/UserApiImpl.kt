package com.spruhs.user.data

import com.spruhs.user.application.User
import com.spruhs.user.application.UserApi
import com.spruhs.user.application.UserGroupInfo
import com.spruhs.user.application.UserRole
import com.spruhs.user.application.UserStatus
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import kotlinx.serialization.Serializable

class UserApiImpl(
    private val userAuthService: UserAuthService,
    private val userNoAuthService: UserNoAuthService
) : UserApi {
    override suspend fun getUser(id: String): User = userAuthService.getUser(id).toUser()

    override suspend fun registerUser(nickName: String, email: String, password: String): String =
        userNoAuthService.registerUser(RegisterUserRequest(nickName, email, password))

    override suspend fun changeNickName(userId: String, nickname: String) {
        userAuthService.changeNickName(userId, nickname)
    }
}

interface UserAuthService {
    @GET("/api/v1/user/{userId}")
    suspend fun getUser(@Path("userId") userId: String): UserMessage

    @PUT("/api/v1/user/{userId}/nickName")
    suspend fun changeNickName(@Path("userId") userId: String, @Query("nickName") nickName: String)
}

interface UserNoAuthService {
    @POST("/api/v1/user")
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

private fun UserMessage.toUser() = User(
    userId = this.id,
    nickName = this.nickName,
    email = this.email,
    groups = this.groups.associate { it.id to it.toUserGroupInfo() },
    imageUrl = this.imageId
)

private fun GroupInfoMessage.toUserGroupInfo() = UserGroupInfo(
    id = this.id,
    name = this.name,
    userStatus = UserStatus.valueOf(this.userStatus),
    userRole = UserRole.valueOf(this.userRole)
)