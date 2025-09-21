package com.spruhs

interface BaseUIState<S : BaseUIState<S>> {
    val isLoading: Boolean

    fun copyWith(isLoading: Boolean = this.isLoading): S
}