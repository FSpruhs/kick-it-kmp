package com.spruhs.main.di

import com.spruhs.auth.application.AuthTokenRepository
import com.spruhs.main.TopBarViewModel
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.request
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mainModule = module {
    viewModelOf(::TopBarViewModel)

    single(named("NoAuthClient")) {
        HttpClient {
            install(ContentNegotiation) {
                json()
            }

            defaultRequest {
                contentType(ContentType.Application.Json)
            }

            install(Logging) {
                level = LogLevel.ALL
                logger = Logger.DEFAULT
            }
        }
    }

    single(named("AuthClient")) {
        val authRepo: AuthTokenRepository = get()
        HttpClient {
            install(ContentNegotiation) {
                json()
            }

            defaultRequest {
                contentType(ContentType.Application.Json)
            }

            install(Logging) {
                level = LogLevel.ALL
                logger = Logger.DEFAULT
            }

            request {
                val token: String? = authRepo.getTokenSync()?.accessToken
                if (token != null) {
                    headers.append("Authorization", "Bearer $token")
                }
            }
        }
    }

    single(named("NoAuthKtorfit")) {
        Ktorfit
            .Builder()
            .baseUrl("http://10.0.2.2:8085/api/")
            .httpClient(get<HttpClient>(named("NoAuthClient")))
            .build()
    }

    single(named("AuthKtorfit")) {
        Ktorfit
            .Builder()
            .baseUrl("http://10.0.2.2:8085/api/")
            .httpClient(get<HttpClient>(named("AuthClient")))
            .build()
    }
}