package com.spruhs.user.application

import kotlinx.coroutines.flow.StateFlow

interface UserRepository {
    val userState: StateFlow<User?>
    val selectedGroup: StateFlow<SelectedGroup?>
    suspend fun getSelectedGroupOrThrow(): SelectedGroup

    suspend fun loadUser(id: String): User
    suspend fun register(email: String, nickName: String, password: String): String
    suspend fun changeNickname(newNickname: String)
    fun setSelectedGroup(group: UserGroupInfo)
    fun addGroup(group: UserGroupInfo)
    fun removeGroup(groupId: String)
    fun logout()
}