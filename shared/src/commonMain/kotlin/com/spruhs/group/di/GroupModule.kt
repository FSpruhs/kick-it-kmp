package com.spruhs.group.di

import com.spruhs.group.presentation.CreateGroupViewModel
import com.spruhs.group.presentation.GroupViewModel
import com.spruhs.group.presentation.InvitePlayerViewModel
import com.spruhs.group.presentation.PlayerDetailsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val groupModule = module {
    viewModelOf(::CreateGroupViewModel)
    viewModelOf(::GroupViewModel)
    viewModelOf(::InvitePlayerViewModel)
    viewModelOf(::PlayerDetailsViewModel)
}