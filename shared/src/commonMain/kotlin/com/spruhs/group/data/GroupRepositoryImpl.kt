package com.spruhs.group.data

import com.spruhs.group.application.Group
import com.spruhs.group.application.GroupNameEntry
import com.spruhs.group.application.GroupRepository
import com.spruhs.group.application.PlayerDetails
import com.spruhs.user.application.UserRole
import com.spruhs.user.application.UserStatus

class GroupRepositoryImpl(private val groupApiClient: GroupApiClient) : GroupRepository {
    override suspend fun getGroupNames(groupId: String): List<GroupNameEntry> =
        groupApiClient.getGroupNameList(groupId).map { it.toGroupNameEntry() }

    override suspend fun getGroup(groupId: String): Group =
        groupApiClient.getGroup(groupId).toGroup()

    override suspend fun getPlayer(groupId: String, userId: String): PlayerDetails =
        groupApiClient.getGroupPlayer(groupId, userId).toPlayerDetails()

    override suspend fun removePlayer(groupId: String, playerId: String) {
        groupApiClient.removePlayer(groupId, playerId)
    }

    override suspend fun createGroup(groupName: String): String =
        groupApiClient.createGroup(CreateGroupRequest(groupName))

    override suspend fun invitePlayer(groupId: String, email: String) {
        groupApiClient.inviteUser(groupId, email)
    }

    override suspend fun updatePlayer(
        groupId: String,
        playerId: String,
        status: UserStatus,
        role: UserRole
    ) {
        groupApiClient.updatePlayer(groupId, playerId, status.name, role.name)
    }
}

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