package com.spruhs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<E, S>(initialState: S): ViewModel() {

    protected val uiStateMutable = MutableStateFlow(initialState)
    val uiState: StateFlow<S> = uiStateMutable.asStateFlow()

    protected val effectsMutable = MutableSharedFlow<E>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val effects: SharedFlow<E> = effectsMutable.asSharedFlow()

    fun <T> performAction(
        setLoading: ((Boolean) -> Unit)? = null,
        onSuccess: suspend (T) -> Unit,
        onError: suspend (Throwable) -> Unit,
        action: suspend () -> T
    ) {
        viewModelScope.launch {
            setLoading?.invoke(true)
            try {
                val result = action()
                onSuccess(result)
            } catch (e: Throwable) {
                AppLogger.e("BaseViewModel", "Action failed: ${e.message}", e)
                onError(e)
            } finally {
                setLoading?.invoke(false)
            }
        }
    }
}