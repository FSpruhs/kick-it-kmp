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
import com.spruhs.auth.presentation.StartViewModel
import org.koin.dsl.module

val authModule =
    module {
        single<AuthenticateUseCase> { AuthenticateUseCase(get(), get()) }
        single<StartViewModel> { StartViewModel(get(), get()) }
        single<LoginViewModel> { LoginViewModel(get(), get()) }
        single<TokenHelper> { TokenHelper() }
        single<AuthTokenRepository> { AuthTokenRepositoryImpl(get(), get()) }
        single<AuthTokenDao> { get<AuthTokenDatabase>().authTokenDao() }
        single<LoginUseCase> { LoginUseCase(get(), get(), get()) }
        single<AuthApi> { AuthApiImpl() }
    }