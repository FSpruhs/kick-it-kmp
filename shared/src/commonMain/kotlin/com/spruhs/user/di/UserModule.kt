package com.spruhs.user.di

import com.spruhs.user.application.LoadUserUseCase
import com.spruhs.user.application.UserApi
import com.spruhs.user.application.UserRepository
import com.spruhs.user.data.UserApiImpl
import com.spruhs.user.data.UserRepositoryImpl
import org.koin.dsl.module

val userModule = module {
    single { LoadUserUseCase(get()) }
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<UserApi> { UserApiImpl() }
}