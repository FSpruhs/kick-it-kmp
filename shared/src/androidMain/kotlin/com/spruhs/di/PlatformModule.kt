package com.spruhs.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.spruhs.auth.data.AuthTokenDatabase
import com.spruhs.auth.data.getAuthTokenDatabase
import org.koin.dsl.module

actual val platformModule = module {
    single<AuthTokenDatabase> {
        val builder = getDatabaseBuilder(context = get())
        getAuthTokenDatabase(builder)
    }
}

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<AuthTokenDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("auth_token_database.db")

    return Room.databaseBuilder<AuthTokenDatabase>(
        context = appContext,
        name = dbFile.absolutePath,
    )
}