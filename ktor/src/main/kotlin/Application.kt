package com.example

import com.example.plugins.provideHttpClient
import com.example.routes.chatRoutes
import com.example.routes.streamRoutes
import com.example.service.LlamaService
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }

    val client = provideHttpClient()
    val llamaService = LlamaService(client)

    routing {
        get("/") {
            call.respondText("HELLO WORLD!")
        }
        chatRoutes(llamaService)
        streamRoutes(llamaService)
    }
}
