package com.example.routes

import com.example.data.ChatRequest
import com.example.service.LlamaService
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.streamRoutes(llamaService: LlamaService) {
    /* 테스트 용도
    post("/streaming") {
        call.respondTextWriter(contentType = ContentType.Text.Plain) {
            val chunks = listOf("안녕 \n", "나는 \n", "스트리밍 \n", "응답이야")
            for (chunk in chunks) {
                write(chunk) // 클라이언트로 데이터 보냄
                flush() // 버퍼에 쌓지 않고 즉시 전송
                delay(1000)
            }
        }
    }
    */

    post("/streaming") {
//        println(call.receiveText())
        val request = call.receive<ChatRequest>()
        call.respondTextWriter(ContentType.Text.Plain) {
            write(" ") // timeout 방지
            flush()

            llamaService.streamChat(prompt = request.message) { chunk ->
                write(chunk)
                flush()
            }
        }
    }
}