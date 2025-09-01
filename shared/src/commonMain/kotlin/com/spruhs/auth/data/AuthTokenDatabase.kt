package com.spruhs.auth.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(entities = [AuthTokenEntity::class], version = 1)
abstract class AuthTokenDatabase : RoomDatabase() {
    abstract fun authTokenDao(): AuthTokenDao
}

fun getAuthTokenDatabase(builder: RoomDatabase.Builder<AuthTokenDatabase>): AuthTokenDatabase =
    builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()