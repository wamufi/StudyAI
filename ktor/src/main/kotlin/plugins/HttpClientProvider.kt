package com.example.plugins

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.HttpTimeoutConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json

fun provideHttpClient() = HttpClient(CIO) {
    install(HttpTimeout) {
        requestTimeoutMillis =
            HttpTimeoutConfig.INFINITE_TIMEOUT_MS
        socketTimeoutMillis =
            HttpTimeoutConfig.INFINITE_TIMEOUT_MS
    }
    install(ContentNegotiation) {
        json()
    }
    engine {
        requestTimeout = 0
    }
}
