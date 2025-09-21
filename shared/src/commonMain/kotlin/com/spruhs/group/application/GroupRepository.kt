package com.spruhs.group.application

interface GroupRepository {
    suspend fun createGroup(groupName: String): String
}