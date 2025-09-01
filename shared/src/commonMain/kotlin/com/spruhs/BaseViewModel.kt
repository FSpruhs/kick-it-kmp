package com.spruhs

import kotlinx.coroutines.CoroutineScope

expect open class BaseViewModel() {
    val scope: CoroutineScope

    fun <T> performAction(
        setLoading: ((Boolean) -> Unit)? = null,
        onSuccess: suspend (T) -> Unit,
        onError: suspend (Throwable) -> Unit,
        action: suspend () -> T
    )
}