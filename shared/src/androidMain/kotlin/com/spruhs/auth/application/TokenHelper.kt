package com.spruhs.auth.application

import android.util.Log
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.util.Date

actual class TokenHelper {
    private val secretKey = "my-256-bit-secret-my-256-bit-secret"
    private val parser =
        Jwts
            .parser()
            .verifyWith(Keys.hmacShaKeyFor(secretKey.toByteArray()))
            .build()

    actual fun getUserId(token: String): String {
        val jwt = parser.parseSignedClaims(token)
        return jwt.payload.subject
    }

    actual fun isTokenValid(token: String): Boolean {
        try {
            val jwt = parser.parseSignedClaims(token)
            return !jwt.payload.expiration.before(Date())
        } catch (e: ExpiredJwtException) {
            Log.d("TokenHelper", "isTokenValid: ${e.message}")
            return false
        }
    }
}