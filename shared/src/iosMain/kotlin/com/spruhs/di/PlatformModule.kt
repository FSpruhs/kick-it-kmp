package com.spruhs.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.spruhs.auth.data.AuthTokenDatabase
import com.spruhs.auth.data.getAuthTokenDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual val platformModule = module {
    single<AuthTokenDatabase> {
        val builder = getDatabaseBuilder()
        getAuthTokenDatabase(builder)
    }
}

fun getDatabaseBuilder(): RoomDatabase.Builder<AuthTokenDatabase> {
    val dbFilePath = documentDirectory() + "/auth_token_database.db"
    return Room.databaseBuilder<AuthTokenDatabase>(
        name = dbFilePath
    )
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null
    )

    return requireNotNull(documentDirectory?.path)
}