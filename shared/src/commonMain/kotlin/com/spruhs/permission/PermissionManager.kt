package com.spruhs.permission

import com.spruhs.user.application.UserRole

object PermissionManager {
    private val permissions: Map<String, Set<UserRole>> =
        mapOf(
            "groupScreen:addPlayerButton" to setOf(UserRole.COACH),
            "matchScreen:planMatchButton" to setOf(UserRole.COACH),
            "upcomingMatchDetailScreen:cancelMatchButton" to setOf(UserRole.COACH),
            "playerDetailsScreen:playerPropertiesReadOnly" to setOf(UserRole.COACH),
            "upcomingMatchDetailScreen:removePlayerFromCadre" to setOf(UserRole.COACH),
            "upcomingMatchDetailScreen:addPlayerFromCadre" to setOf(UserRole.COACH),
            "matchResultDetailScreen:changeResultButton" to setOf(UserRole.COACH),
            "matchResultDetailScreen:enterResultButton" to setOf(UserRole.COACH)
        )

    fun hasPermission(role: UserRole?, elementId: String): Boolean =
        permissions[elementId]?.contains(role ?: UserRole.PLAYER) ?: false
}