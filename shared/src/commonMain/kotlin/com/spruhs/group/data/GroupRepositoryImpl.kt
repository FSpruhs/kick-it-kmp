package com.spruhs.group.data

import com.spruhs.group.application.GroupRepository

class GroupRepositoryImpl : GroupRepository {
    override suspend fun createGroup(groupName: String): String {
        TODO("Not yet implemented")
    }
}