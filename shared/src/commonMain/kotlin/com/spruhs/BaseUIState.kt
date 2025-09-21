package com.spruhs

interface BaseUIState<S : BaseUIState<S>> {
    val isLoading: Boolean
    val error: String?

    fun copyWith(isLoading: Boolean = this.isLoading, error: String? = this.error): S
}