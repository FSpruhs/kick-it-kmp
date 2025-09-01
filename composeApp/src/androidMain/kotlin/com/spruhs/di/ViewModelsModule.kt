package com.spruhs.di

import com.spruhs.auth.presentation.LoginViewModel
import com.spruhs.auth.presentation.StartViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule =
    module {
        viewModel { StartViewModel(get(), get()) }
        viewModel { LoginViewModel(get()) }
    }