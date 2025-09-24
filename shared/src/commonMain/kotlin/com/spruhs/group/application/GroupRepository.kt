package com.spruhs.group.application

import com.spruhs.user.application.UserRole
import com.spruhs.user.application.UserStatus

interface GroupRepository {
    suspend fun getGroupNames(groupId: String): List<GroupNameEntry>
    suspend fun getGroup(groupId: String): Group
    suspend fun getPlayer(groupId: String, userId: String): PlayerDetails
    suspend fun removePlayer(groupId: String, playerId: String)
    suspend fun createGroup(groupName: String): String
    suspend fun invitePlayer(groupId: String, email: String)
    suspend fun updatePlayer(groupId: String, playerId: String, status: UserStatus, role: UserRole)
}