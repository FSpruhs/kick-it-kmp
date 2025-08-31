package com.spruhs.di

import org.koin.core.module.dsl.viewModel
import com.spruhs.auth.presentation.StartViewModel
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel { StartViewModel(get()) }
}