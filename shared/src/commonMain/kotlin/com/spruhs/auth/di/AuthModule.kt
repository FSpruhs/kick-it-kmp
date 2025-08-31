package com.spruhs.auth.di

import com.spruhs.auth.application.AuthenticateUseCase
import com.spruhs.auth.presentation.StartViewModel
import org.koin.dsl.module

val authModule =
    module {
        single<AuthenticateUseCase> { AuthenticateUseCase() }
        single<StartViewModel> { StartViewModel(get()) }
    }