package com.wamufi.studyai.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wamufi.studyai.SummaryResponse
import com.wamufi.studyai.TextRequest
import com.wamufi.studyai.client
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TextSummerizeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow("result")
    val uiState = _uiState.asStateFlow()

    suspend fun summarize(text: String): String {
        val response = client.post("http://10.0.2.2:8000/summarize") {
            contentType(ContentType.Application.Json)
            setBody(TextRequest(text))
        }
        Log.d("API", response.bodyAsText())

        return response.body<SummaryResponse>().summary
    }

    fun startSummary(message: String) {
        viewModelScope.launch {
            try {
                val result = summarize(message)
                Log.d("API", "result: $result")
                _uiState.value = result
            } catch (e: Exception) {
                Log.e("Error", e.localizedMessage)
                _uiState.value = e.localizedMessage ?: "Error"
            }
        }
    }
}