package com.spruhs.group.data

import com.spruhs.group.application.Group
import com.spruhs.group.application.GroupNameEntry
import com.spruhs.group.application.GroupRepository
import com.spruhs.group.application.PlayerDetails
import com.spruhs.user.application.UserRole
import com.spruhs.user.application.UserStatus

class GroupRepositoryImpl(private val groupService: GroupService) : GroupRepository {
    override suspend fun getGroupNames(groupId: String): List<GroupNameEntry> =
        groupService.getGroupNameList(groupId)

    override suspend fun getGroup(groupId: String): Group = groupService.getGroup(groupId)

    override suspend fun getPlayer(groupId: String, userId: String): PlayerDetails =
        groupService.getGroupPlayer(groupId, userId)

    override suspend fun removePlayer(groupId: String, playerId: String) {
        groupService.removePlayer(groupId, playerId)
    }

    override suspend fun createGroup(groupName: String): String =
        groupService.createGroup(groupName)

    override suspend fun invitePlayer(groupId: String, email: String) {
        groupService.inviteUser(groupId, email)
    }

    override suspend fun updatePlayer(
        groupId: String,
        playerId: String,
        status: UserStatus,
        role: UserRole
    ) {
        groupService.updatePlayer(groupId, playerId, status, role)
    }
}