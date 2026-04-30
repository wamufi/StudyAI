package com.example.data

import kotlinx.serialization.Serializable

@Serializable
data class ChatRequest(val message: String)

@Serializable
data class ChatResponse(val reply: String)
