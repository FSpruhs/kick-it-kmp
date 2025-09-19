package com.spruhs.group.application

import com.spruhs.user.application.UserRole
import com.spruhs.user.application.UserStatus

data class PlayerDetails(
    val id: String,
    val status: UserStatus,
    val role: UserRole,
    val avatarUrl: String? = null,
    val email: String
)