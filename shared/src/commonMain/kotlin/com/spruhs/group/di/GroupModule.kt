package com.spruhs.group.di

import com.spruhs.group.GroupViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val groupModule = module {
    viewModelOf(::GroupViewModel)
}