package com.spruhs.user.di

import com.spruhs.user.application.ChangeNicknameUseCase
import com.spruhs.user.application.GetUpcomingMatchesUseCase
import com.spruhs.user.application.LoadUserUseCase
import com.spruhs.user.application.LogoutUseCase
import com.spruhs.user.application.SelectGroupUseCase
import com.spruhs.user.application.UserRepository
import com.spruhs.user.data.UserAuthApiClient
import com.spruhs.user.data.UserNoAuthApiClient
import com.spruhs.user.data.UserRepositoryImpl
import com.spruhs.user.data.createUserAuthApiClient
import com.spruhs.user.data.createUserNoAuthApiClient
import com.spruhs.user.presentation.HomeViewModel
import com.spruhs.user.presentation.ProfileViewModel
import com.spruhs.user.presentation.SelectGroupViewModel
import de.jensklingenberg.ktorfit.Ktorfit
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val userModule = module {
    single { LoadUserUseCase(get()) }
    single { ChangeNicknameUseCase(get()) }
    single { GetUpcomingMatchesUseCase(get()) }
    single { LogoutUseCase(get()) }
    single { SelectGroupUseCase(get()) }
    single<UserRepository> { UserRepositoryImpl(get(), get()) }

    single<UserAuthApiClient> {
        get<Ktorfit>(named("AuthKtorfit")).createUserAuthApiClient()
    }

    single<UserNoAuthApiClient> {
        get<Ktorfit>(named("NoAuthKtorfit")).createUserNoAuthApiClient()
    }

    viewModelOf(::HomeViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::SelectGroupViewModel)
}