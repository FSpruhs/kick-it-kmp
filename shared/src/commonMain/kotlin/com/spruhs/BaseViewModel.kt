package com.spruhs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<I, E, S : BaseUIState<S>>(initialState: S) : ViewModel() {

    protected val uiStateMutable = MutableStateFlow(initialState)
    open val uiState: StateFlow<S> = uiStateMutable.asStateFlow()

    protected val effectsMutable = MutableSharedFlow<E>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val effects: SharedFlow<E> = effectsMutable.asSharedFlow()

    open fun processIntent(intent: I) {}

    fun <T> performAction(
        onSuccess: suspend (T) -> Unit,
        onError: suspend (Throwable) -> Unit = {},
        action: suspend () -> T
    ) {
        viewModelScope.launch {
            uiStateMutable.update { it.copyWith(isLoading = true) }
            try {
                val result = action()
                onSuccess(result)
                uiStateMutable.update { it.copyWith(error = null) }
            } catch (e: Throwable) {
                AppLogger.e("BaseViewModel", "Action failed: ${e.message}", e)
                onError(e)
                uiStateMutable.update { it.copyWith(error = e.message) }
            } finally {
                uiStateMutable.update { it.copyWith(isLoading = false) }
            }
        }
    }
}