package com.spruhs.auth.application

import kotlinx.coroutines.delay

class AuthenticateUseCase {

    suspend fun authenticate(): Boolean {
        delay(2000)
        return true
    }
}