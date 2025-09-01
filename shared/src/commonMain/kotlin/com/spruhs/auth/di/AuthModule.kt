package com.spruhs.auth.di

import com.spruhs.auth.application.AuthApi
import com.spruhs.auth.application.AuthTokenRepository
import com.spruhs.auth.application.AuthenticateUseCase
import com.spruhs.auth.application.LoginUseCase
import com.spruhs.auth.application.TokenHelper
import com.spruhs.auth.data.AuthApiImpl
import com.spruhs.auth.data.AuthTokenDao
import com.spruhs.auth.data.AuthTokenDatabase
import com.spruhs.auth.data.AuthTokenRepositoryImpl
import com.spruhs.auth.presentation.LoginViewModel
import com.spruhs.auth.presentation.RegisterViewModel
import com.spruhs.auth.presentation.StartViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authModule =
    module {
        single { AuthenticateUseCase(get(), get()) }
        single { LoginUseCase(get(), get(), get()) }
        single { TokenHelper() }
        single<AuthTokenRepository> { AuthTokenRepositoryImpl(get(), get()) }
        single<AuthTokenDao> { get<AuthTokenDatabase>().authTokenDao() }
        single<AuthApi> { AuthApiImpl() }

        viewModelOf(::StartViewModel)
        viewModelOf(::RegisterViewModel)
        viewModelOf(::LoginViewModel)
    }