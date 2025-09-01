package com.spruhs.user.application

enum class UserRole(override val label: String) : LabeledEnum {
    COACH("Coach"),
    PLAYER("Player")
}