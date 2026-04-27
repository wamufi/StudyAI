package com.wamufi.studyai.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.wamufi.studyai.navigation.Screen
import com.wamufi.studyai.viewmodel.ChatViewModel

@Composable
fun ChatScreen(navController: NavController, viewModel: ChatViewModel = viewModel()) {
    var input by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    AppScaffold(Screen.ChatScreen.title, true, { navController.navigateUp() }) {
        Column {
            TextField(modifier = Modifier.fillMaxWidth(), value = input, onValueChange = { input = it })

            Button(onClick = {
                viewModel.sendMessage(input) {
                    result = it
                }
            }) {
                Text("Send")
            }

            Text(text = result)
        }
    }
}