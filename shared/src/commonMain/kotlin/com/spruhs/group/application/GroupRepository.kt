package com.spruhs.group.application

interface GroupRepository {
    suspend fun getGroupNames(groupId: String): List<GroupNameEntry>
    suspend fun getGroup(groupId: String): Group
    suspend fun removePlayer(groupId: String, playerId: String)
    suspend fun createGroup(groupName: String): String
    suspend fun invitePlayer(groupId: String, email: String)
}