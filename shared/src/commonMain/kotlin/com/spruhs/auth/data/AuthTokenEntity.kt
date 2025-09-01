package com.spruhs.auth.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "auth_token")
data class AuthTokenEntity(
    @PrimaryKey val id: Int = 1,
    val accessToken: String,
    val refreshToken: String
)
