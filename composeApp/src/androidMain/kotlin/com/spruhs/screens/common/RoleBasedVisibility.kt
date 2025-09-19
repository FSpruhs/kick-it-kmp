package com.spruhs.screens.common

import androidx.compose.runtime.Composable
import com.spruhs.permission.PermissionManager
import com.spruhs.user.application.UserRole

@Composable
fun RoleBasedVisibility(role: UserRole?, elementId: String, content: @Composable () -> Unit) {
    if (PermissionManager.hasPermission(role ?: UserRole.PLAYER, elementId)) {
        content()
    }
}