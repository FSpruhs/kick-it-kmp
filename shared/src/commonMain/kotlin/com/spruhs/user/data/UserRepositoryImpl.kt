package com.spruhs.user.data

import com.spruhs.user.application.SelectedGroup
import com.spruhs.user.application.User
import com.spruhs.user.application.UserApi
import com.spruhs.user.application.UserGroupInfo
import com.spruhs.user.application.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class UserRepositoryImpl(private val userApi: UserApi) : UserRepository {
    private val _userState = MutableStateFlow<User?>(null)
    override val userState: StateFlow<User?> = _userState

    private val _selectedGroup = MutableStateFlow<SelectedGroup?>(null)
    override val selectedGroup: StateFlow<SelectedGroup?> = _selectedGroup

    override suspend fun getSelectedGroupOrThrow(): SelectedGroup =
        selectedGroup.value ?: throw IllegalStateException("No selected group")

    override suspend fun getUserOrThrow(): User =
        userState.value ?: throw IllegalStateException("No user")

    override suspend fun loadUser(id: String): User = userApi.getUser(id).also { user ->
        _userState.value = user

        user.groups.values
            .sortedByDescending { it.lastMatch }
            .firstOrNull()
            ?.let {
                setSelectedGroup(it)
            }
    }

    override suspend fun register(email: String, nickName: String, password: String) =
        userApi.registerUser(nickName, email, password)

    override suspend fun changeNickname(newNickname: String) {
        userApi.changeNickName(userState.value?.userId ?: "", newNickname)
        _userState.update { it?.copy(nickName = newNickname) }
    }

    override fun setSelectedGroup(group: UserGroupInfo) {
        _selectedGroup.update { SelectedGroup(group.id, group.name, group.userRole) }
    }

    override fun addGroup(group: UserGroupInfo) {
        _userState.update { current ->
            val updatedGroups =
                current?.groups
                    .orEmpty()
                    .toMutableMap()
            updatedGroups[group.id] = group
            current?.copy(groups = updatedGroups)
        }
    }

    override fun removeGroup(groupId: String) {
        _userState.update { current ->
            val updatedGroups =
                current
                    ?.groups
                    .orEmpty()
                    .toMutableMap()
            updatedGroups.remove(groupId)
            current?.copy(
                groups = updatedGroups
            )
        }
    }

    override fun logout() {
        TODO("Not yet implemented")
    }
}