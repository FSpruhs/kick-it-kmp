package com.spruhs.statistics.di

import com.spruhs.statistics.application.StatisticsRepository
import com.spruhs.statistics.data.StatisticsRepositoryImpl
import org.koin.dsl.module

val statisticModule = module {
    single<StatisticsRepository> { StatisticsRepositoryImpl() }
}