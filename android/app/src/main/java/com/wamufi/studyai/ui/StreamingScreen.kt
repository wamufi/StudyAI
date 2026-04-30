package com.wamufi.studyai.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.wamufi.studyai.navigation.Screen
import com.wamufi.studyai.viewmodel.StreamingViewModel

@Composable
fun StreamingScreen(navController: NavController, viewModel: StreamingViewModel = viewModel()) {

    var input by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
//    val result by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.chunks.collect { result += it }
    }

    AppScaffold(Screen.StreamingScreen.title, true, { navController.navigateUp() }) {
        Column {
            TextField(modifier = Modifier.fillMaxWidth(), value = input, onValueChange = { input = it })

            Button(onClick = {
                viewModel.startStreaming(input)
            }) {
                Text("Start Streaming")
            }

            Text(text = result)
        }
    }
}