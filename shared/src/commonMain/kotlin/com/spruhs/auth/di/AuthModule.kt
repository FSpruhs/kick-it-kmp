package com.spruhs.auth.di

import androidx.room.Room
import com.spruhs.auth.application.AuthenticateUseCase
import com.spruhs.auth.application.AuthenticationRepository
import com.spruhs.auth.application.TokenHelper
import com.spruhs.auth.application.AuthTokenRepository
import com.spruhs.auth.application.UserRepository
import com.spruhs.auth.data.AuthTokenDao
import com.spruhs.auth.data.AuthTokenDatabase
import com.spruhs.auth.data.AuthenticationRepositoryImpl
import com.spruhs.auth.data.AuthTokenRepositoryImpl
import com.spruhs.auth.data.UserRepositoryImpl
import com.spruhs.auth.presentation.StartViewModel
import org.koin.dsl.module

val authModule =
    module {
        single<AuthenticateUseCase> { AuthenticateUseCase(get(), get(), get(), get()) }
        single<StartViewModel> { StartViewModel(get()) }
        single<TokenHelper> { TokenHelper() }
        single<AuthTokenRepository> { AuthTokenRepositoryImpl(get()) }
        single<UserRepository> { UserRepositoryImpl() }
        single<AuthenticationRepository> { AuthenticationRepositoryImpl() }
        single<AuthTokenDao> { get<AuthTokenDatabase>().authTokenDao() }
    }