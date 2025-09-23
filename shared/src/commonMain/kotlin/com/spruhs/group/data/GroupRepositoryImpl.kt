package com.spruhs.group.data

import com.spruhs.group.application.Group
import com.spruhs.group.application.GroupNameEntry
import com.spruhs.group.application.GroupRepository
import com.spruhs.user.application.UserRole
import com.spruhs.user.application.UserStatus

class GroupRepositoryImpl : GroupRepository {
    override suspend fun getGroupNames(groupId: String): List<GroupNameEntry> {
        TODO("Not yet implemented")
    }

    override suspend fun getGroup(groupId: String): Group {
        TODO("Not yet implemented")
    }

    override suspend fun removePlayer(groupId: String, playerId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun createGroup(groupName: String): String {
        TODO("Not yet implemented")
    }

    override suspend fun invitePlayer(groupId: String, email: String) {
        TODO("Not yet implemented")
    }

    override suspend fun updatePlayer(
        groupId: String,
        playerId: String,
        status: UserStatus,
        role: UserRole
    ) {
        TODO("Not yet implemented")
    }
}