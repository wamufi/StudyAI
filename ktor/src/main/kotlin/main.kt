package com.example

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.net.HttpURLConnection
import java.net.URL

/*
fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}
*/

@Serializable
data class ChatRequest(val message: String)

@Serializable
data class ChatResponse(val reply: String)

fun main() {
    embeddedServer(Netty, 8080) {
        install(ContentNegotiation) {
            json()
        }

        routing {
            get("/") {
                call.respondText("HELLO WORLD!")
            }
            post("/chat") {
                val req = call.receive<ChatRequest>()
                val reply = callOpenAI(req.message)
                call.respond(ChatResponse(reply))
            }
        }
    }.start(wait = true)
}

fun callOpenAI(message: String): String {
    val apiKey = "lm-studio"

    val url = URL("http://127.0.0.1:1234/v1/chat/completions")
    val connection = url.openConnection() as HttpURLConnection

    connection.requestMethod = "POST"
    connection.setRequestProperty("Authorization", "Bearer $apiKey")
    connection.setRequestProperty("Content-Type", "application/json")
    connection.doOutput = true

    val body = """
        {
          "model": "google/gemma-4-e4b",
          "messages": [
            {"role": "user", "content": "$message"}
          ]
        }
    """.trimIndent()

    connection.getOutputStream().use {
        it.write(body.toByteArray())
    }

    val response = connection.getInputStream().bufferedReader().readText()

    val json = Json.parseToJsonElement(response)
    return json.jsonObject["choices"]!!.jsonArray[0].jsonObject["message"]!!.jsonObject["content"]!!.jsonPrimitive.content
}