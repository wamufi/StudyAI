package com.wamufi.studyai.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wamufi.studyai.TextRequest
import com.wamufi.studyai.client
import io.ktor.client.request.preparePost
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.utils.io.readAvailable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class RagViewModel : ViewModel() {

    private val _chunks = MutableSharedFlow<String>()
    val chunks = _chunks.asSharedFlow()

    fun startRag(message: String) {
        viewModelScope.launch {
            _chunks.emit("\n")

            val response = client.preparePost("http://10.0.2.2:8000/rag") {
                contentType(ContentType.Application.Json)
                setBody(TextRequest(message))
            }

            response.execute { response ->
                Log.d("API", "response: $response")
                val channel = response.bodyAsChannel()

                val buffer = ByteArray(1024)
                while (!channel.isClosedForRead) {
                    val bytesRead = channel.readAvailable(buffer)
                    if (bytesRead > 0) {
//                        val chunk = String(buffer, 0, bytesRead)
                        val chunk = buffer.decodeToString(0, bytesRead)
                        Log.d("API", "chunk: $chunk")
                        _chunks.emit(chunk)
                    }
                }
            }
        }
    }
}