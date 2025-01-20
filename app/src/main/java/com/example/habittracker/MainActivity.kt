package com.example.habittracker

import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
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
import androidx.core.app.ActivityCompat.requestPermissions
import com.example.habittracker.ui.HabitReminderWorker
import com.example.habittracker.ui.MainScreen
import com.example.habittracker.ui.PreferenceManager
import com.example.habittracker.ui.QuoteViewModel
import com.example.habittracker.ui.TutorialScreen
import com.example.habittracker.ui.theme.HabittrackerTheme
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val quotes = QuoteViewModel.getQuotesFromAssets(this)
        val randomQuote = quotes.random()
        setContent {
            val context = LocalContext.current
            val showTutorial = remember { mutableStateOf(PreferenceManager.isFirstLaunch(context)) }

            createNotificationChannel()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestNotificationPermission(this)
            }

            val workRequest = PeriodicWorkRequestBuilder<HabitReminderWorker>(24, TimeUnit.HOURS)
                .setInitialDelay(calculateInitialDelay(), TimeUnit.MILLISECONDS)
                .build()

            WorkManager.getInstance(this).enqueue(workRequest)

            LaunchedEffect(Unit) {
                if (showTutorial.value) {
                    PreferenceManager.setFirstLaunch(context, false)
                }
            }

            HabittrackerTheme {
                if (showTutorial.value) {
                    TutorialScreen(onFinish = { showTutorial.value = false })
                } else {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        MainScreen(
                            modifier = Modifier.padding(innerPadding),
                            randomQuote = randomQuote
                        )
                    }
                }
            }
        }
    }

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Habit Reminder"
            val descriptionText = "Channel for habit reminders"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel =
                NotificationChannel("HABIT_REMINDER_CHANNEL", name, importance).apply {
                    description = descriptionText
                }
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun calculateInitialDelay(): Long {
        val currentTime = System.currentTimeMillis()
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 18)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        if (calendar.timeInMillis <= currentTime) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        return calendar.timeInMillis - currentTime
    }
}


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun requestNotificationPermission(activity: MainActivity) {
    requestPermissions(activity, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
}