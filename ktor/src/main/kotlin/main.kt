package com.example

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

/*
fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}
*/

fun main() {
    embeddedServer(Netty, 8080, module = Application::module).start(wait = true)
}
