package com.example.service

import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.preparePost
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.utils.io.readUTF8Line
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.net.HttpURLConnection
import java.net.URL

class LlamaService(private val client: HttpClient) {
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

    suspend fun streamChat(prompt: String, onChunk: suspend (String) -> Unit) {
        val apiKey = "lm-studio"

        val response = client.preparePost("http://127.0.0.1:8081/v1/chat/completions") {
//            header("Authorization", "Bearer $apiKey")
            contentType(ContentType.Application.Json)

            setBody("""
            {
              "model": "google/gemma-4-e4b",
              "messages": [
                {"role": "user", "content": "$prompt"}
              ],
              "stream": true
            }
        """.trimIndent())
        }

        println("response: $response")

        response.execute { httpResponse ->
            val channel = httpResponse.bodyAsChannel()
            while (true) {
                val line = channel.readUTF8Line() ?: break
                println(line)

                if (!line.startsWith("data:")) continue
                if (line.contains("[DONE]")) break

                val json = line.removePrefix("data: ").trim()
                val content = extractContent(json)
                println("content: $content")

                if (!content.isNullOrEmpty()) onChunk(content)
            }
        }
    }

    private fun extractContent(json: String): String? {
        return Regex("\"reasoning_content\":\"(.*?)\"")
            .find(json)
            ?.groupValues
            ?.get(1)
    }
}