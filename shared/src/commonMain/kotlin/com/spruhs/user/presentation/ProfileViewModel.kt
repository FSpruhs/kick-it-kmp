package com.spruhs.user.presentation

import androidx.lifecycle.viewModelScope
import com.spruhs.BaseUIState
import com.spruhs.BaseViewModel
import com.spruhs.user.application.UserRepository
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(private val userRepository: UserRepository) :
    BaseViewModel<ProfileEffect, ProfileUIState>(ProfileUIState()) {

    fun processIntent(intent: ProfileIntent) {
        viewModelScope.launch {
            when (intent) {
                is ProfileIntent.Logout -> {
                    userRepository.logout()
                    effectsMutable.emit(ProfileEffect.Logout)
                }

                is ProfileIntent.ChangeNewNickname -> uiStateMutable.update {
                    it.copy(newNickName = intent.newNickname)
                }
                is ProfileIntent.ChangeNickname -> {
                    if (uiState.value.newNickName != uiState.value.nickName) {
                        userRepository.changeNickname(uiState.value.newNickName)
                    }
                }
            }
        }
    }
}

data class ProfileUIState(
    override val isLoading: Boolean = false,
    override val error: String? = null,
    val nickName: String = "",
    val newNickName: String = "",
    val imageUrl: String? = null
) : BaseUIState

sealed class ProfileIntent {
    object Logout : ProfileIntent()
    object ChangeNickname : ProfileIntent()
    data class ChangeNewNickname(val newNickname: String) : ProfileIntent()
}

sealed class ProfileEffect {
    object Logout : ProfileEffect()
}