package com.spruhs

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

actual open class BaseViewModel {
    actual val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    actual fun <T> performAction(
        setLoading: ((Boolean) -> Unit)?,
        onSuccess: suspend (T) -> Unit,
        onError: suspend (Throwable) -> Unit,
        action: suspend () -> T
    ) {
        scope.launch {
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

    fun clear() {
        scope.cancel()
    }
}