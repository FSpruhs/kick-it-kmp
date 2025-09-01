package com.spruhs.auth.application

data class AuthToken(val accessToken: String, val refreshToken: String)