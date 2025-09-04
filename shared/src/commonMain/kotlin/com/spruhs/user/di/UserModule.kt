package com.spruhs.user.di

import com.spruhs.user.application.LoadUserUseCase
import com.spruhs.user.application.UserApi
import com.spruhs.user.application.UserRepository
import com.spruhs.user.data.UserApiImpl
import com.spruhs.user.data.UserAuthService
import com.spruhs.user.data.UserNoAuthService
import com.spruhs.user.data.UserRepositoryImpl
import com.spruhs.user.data.createUserAuthService
import com.spruhs.user.data.createUserNoAuthService
import de.jensklingenberg.ktorfit.Ktorfit
import org.koin.core.qualifier.named
import org.koin.dsl.module

val userModule = module {
    single { LoadUserUseCase(get()) }
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<UserApi> { UserApiImpl(get(), get()) }

    single<UserAuthService> {
        get<Ktorfit>(named("AuthKtorfit")).createUserAuthService()
    }

    single<UserNoAuthService> {
        get<Ktorfit>(named("NoAuthKtorfit")).createUserNoAuthService()
    }
}