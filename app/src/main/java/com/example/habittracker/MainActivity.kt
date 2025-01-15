package com.example.habittracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.habittracker.ui.MainScreen
import com.example.habittracker.ui.PreferenceManager
import com.example.habittracker.ui.QuoteViewModel
import com.example.habittracker.ui.TutorialScreen
import com.example.habittracker.ui.theme.HabittrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val quotes = QuoteViewModel.getQuotesFromAssets(this)
        val randomQuote = quotes.random()
        setContent {
            val context = LocalContext.current
            val showTutorial = remember { mutableStateOf(PreferenceManager.isFirstLaunch(context)) }

            LaunchedEffect(Unit) {
                if(showTutorial.value) {
                    PreferenceManager.setFirstLaunch(context, false)
                }
            }

            if(showTutorial.value) {
                TutorialScreen ( onFinish = { showTutorial.value = false } )
            } else {
                HabittrackerTheme {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        MainScreen(modifier = Modifier.padding(innerPadding), randomQuote = randomQuote)
                    }
                }
            }

        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HabittrackerTheme {
        Greeting("Android")
    }
}