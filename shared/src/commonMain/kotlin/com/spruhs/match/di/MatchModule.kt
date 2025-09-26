package com.spruhs.match.di

import com.spruhs.match.application.EnterResultUseCase
import com.spruhs.match.application.GetEnterResultDataUseCase
import com.spruhs.match.presentation.EnterMatchResultViewModel
import com.spruhs.match.presentation.MatchResultDetailViewModel
import com.spruhs.match.presentation.PlanMatchViewModel
import com.spruhs.match.presentation.UpcomingMatchDetailsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val matchModule = module {
    single { GetEnterResultDataUseCase(get(), get(), get()) }
    single { EnterResultUseCase(get()) }

    viewModelOf(::EnterMatchResultViewModel)
    viewModelOf(::MatchResultDetailViewModel)
    viewModelOf(::UpcomingMatchDetailsViewModel)
    viewModelOf(::UpcomingMatchDetailsViewModel)
    viewModelOf(::PlanMatchViewModel)
}