package com.spruhs.group.di

import com.spruhs.group.application.CreateGroupUseCase
import com.spruhs.group.application.GetGroupDataUseCase
import com.spruhs.group.application.GetPlayerDetailsUseCase
import com.spruhs.group.application.GroupRepository
import com.spruhs.group.application.InvitePlayerUseCase
import com.spruhs.group.application.LeaveGroupUseCase
import com.spruhs.group.application.UpdatePlayerUseCase
import com.spruhs.group.data.GroupApi
import com.spruhs.group.data.GroupRepositoryImpl
import com.spruhs.group.data.GroupService
import com.spruhs.group.data.createGroupApi
import com.spruhs.group.presentation.CreateGroupViewModel
import com.spruhs.group.presentation.GroupViewModel
import com.spruhs.group.presentation.InvitePlayerViewModel
import com.spruhs.group.presentation.PlayerDetailsViewModel
import de.jensklingenberg.ktorfit.Ktorfit
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val groupModule = module {
    single { CreateGroupUseCase(get(), get()) }
    single { LeaveGroupUseCase(get(), get()) }
    single { GetGroupDataUseCase(get(), get()) }
    single { GetPlayerDetailsUseCase(get(), get(), get(), get()) }
    single { InvitePlayerUseCase(get(), get()) }
    single { UpdatePlayerUseCase(get(), get()) }

    single<GroupApi> {
        get<Ktorfit>(named("AuthKtorfit")).createGroupApi()
    }

    single { GroupService(get()) }
    single<GroupRepository> { GroupRepositoryImpl(get()) }

    viewModelOf(::CreateGroupViewModel)
    viewModelOf(::GroupViewModel)
    viewModelOf(::InvitePlayerViewModel)
    viewModelOf(::PlayerDetailsViewModel)
}