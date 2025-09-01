package com.spruhs.user.di

import com.spruhs.user.application.LoadUserUseCase
import com.spruhs.user.application.UserRepository
import com.spruhs.user.data.UserApi
import com.spruhs.user.data.UserRepositoryImpl
import org.koin.dsl.module

val userModule = module {
    single<LoadUserUseCase> { LoadUserUseCase(get()) }
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<UserApi> { UserApi() }
}