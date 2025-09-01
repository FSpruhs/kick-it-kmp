package com.spruhs.user.application

import kotlinx.datetime.LocalDateTime

data class User(
    val userId: String,
    val nickName: String,
    val email: String,
    val groups: Map<String, UserGroupInfo>,
    val imageUrl: String? = null
)

data class UserGroupInfo(
    val id: String,
    val name: String,
    val userStatus: UserStatus,
    val userRole: UserRole,
    val lastMatch: LocalDateTime? = null
)

interface LabeledEnum {
    val label: String
}