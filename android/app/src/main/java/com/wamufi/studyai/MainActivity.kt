package com.wamufi.studyai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.wamufi.studyai.navigation.AppNavGraph
import com.wamufi.studyai.navigation.MAIN_SCREENS
import com.wamufi.studyai.navigation.Screen
import com.wamufi.studyai.ui.AppScaffold
import com.wamufi.studyai.ui.theme.StudyAITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudyAITheme {
                val navController = rememberNavController()
                AppNavGraph(navController)
            }
        }
    }
}

@Composable
fun MainScreen(navController: NavController) {
    AppScaffold(Screen.Main.title, false, { }) {
        LazyColumn {
            items(MAIN_SCREENS) { screen ->
                Text(text = screen.title, modifier = Modifier.fillMaxWidth().clickable {
                        navController.navigate(screen.route)
                    }.padding(16.dp))
            }
        }
    }
}