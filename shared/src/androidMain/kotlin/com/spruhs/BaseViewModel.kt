package com.spruhs

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

actual open class BaseViewModel : ViewModel() {
    actual val scope = viewModelScope

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
                Log.e("BaseViewModel", "Action failed: ${e.message}", e)
                onError(e)
            } finally {
                setLoading?.invoke(false)
            }
        }
    }
}