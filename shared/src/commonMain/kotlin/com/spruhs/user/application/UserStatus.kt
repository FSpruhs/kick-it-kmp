package com.spruhs.user.application

enum class UserStatus(override val label: String) : LabeledEnum {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    REMOVED("Removed"),
    LEAVED("Leaved")
}