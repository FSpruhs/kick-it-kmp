package com.spruhs.auth.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AuthTokenDao {
    @Query("SELECT * FROM auth_token WHERE id = 1")
    suspend fun getToken(): AuthTokenEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveToken(token: AuthTokenEntity)

    @Query("DELETE FROM auth_token")
    suspend fun deleteToken()
}