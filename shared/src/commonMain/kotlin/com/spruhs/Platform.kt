package com.spruhs

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform