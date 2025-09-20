package com.spruhs.match.di

import com.spruhs.match.presentation.EnterMatchResultViewModel
import com.spruhs.match.presentation.MatchResultDetailViewModel
import com.spruhs.match.presentation.UpcomingMatchDetailsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val matchModule = module {
    viewModelOf(::EnterMatchResultViewModel)
    viewModelOf(::MatchResultDetailViewModel)
    viewModelOf(::UpcomingMatchDetailsViewModel)
}