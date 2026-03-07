package com.spruhs.statistics.di

import com.spruhs.statistics.application.StatisticsRepository
import com.spruhs.statistics.data.StatisticsApi
import com.spruhs.statistics.data.StatisticsRepositoryImpl
import com.spruhs.statistics.data.createStatisticsApi
import de.jensklingenberg.ktorfit.Ktorfit
import org.koin.core.qualifier.named
import org.koin.dsl.module

val statisticModule = module {
    single<StatisticsRepository> { StatisticsRepositoryImpl(get()) }

    single<StatisticsApi> {
        get<Ktorfit>(named("AuthKtorfit")).createStatisticsApi()
    }
}