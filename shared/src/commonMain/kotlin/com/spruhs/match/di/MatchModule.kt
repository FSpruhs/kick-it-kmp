package com.spruhs.match.di

import com.spruhs.match.application.EnterResultUseCase
import com.spruhs.match.application.GetEnterResultDataUseCase
import com.spruhs.match.application.GetMatchResultDetailsUseCase
import com.spruhs.match.application.GetMatchesDataUseCases
import com.spruhs.match.application.MatchRepository
import com.spruhs.match.data.MatchRepositoryImpl
import com.spruhs.match.data.MatchService
import com.spruhs.match.data.createMatchService
import com.spruhs.match.presentation.EnterMatchResultViewModel
import com.spruhs.match.presentation.MatchResultDetailViewModel
import com.spruhs.match.presentation.MatchViewModel
import com.spruhs.match.presentation.PlanMatchViewModel
import com.spruhs.match.presentation.UpcomingMatchDetailsViewModel
import de.jensklingenberg.ktorfit.Ktorfit
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val matchModule = module {
    single { GetEnterResultDataUseCase(get(), get(), get()) }
    single { EnterResultUseCase(get()) }
    single { GetMatchResultDetailsUseCase(get(), get(), get()) }
    single { GetMatchesDataUseCases(get(), get()) }
    single<MatchRepository> { MatchRepositoryImpl(get()) }
    single<MatchService> { get<Ktorfit>(named("AuthKtorfit")).createMatchService() }

    viewModelOf(::EnterMatchResultViewModel)
    viewModelOf(::MatchResultDetailViewModel)
    viewModelOf(::UpcomingMatchDetailsViewModel)
    viewModelOf(::UpcomingMatchDetailsViewModel)
    viewModelOf(::PlanMatchViewModel)
    viewModelOf(::MatchViewModel)
}