package com.example.routes

import com.example.data.ChatRequest
import com.example.data.ChatResponse
import com.example.service.LlamaService
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.chatRoutes(llamaService: LlamaService) {
    post("/chat") {
        val req = call.receive<ChatRequest>()
        val reply = llamaService.callOpenAI(req.message)
        call.respond(ChatResponse(reply))
    }
}