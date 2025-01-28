package com.example.habittracker

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat.requestPermissions
import com.example.habittracker.ui.HabitReminderReceiver
import com.example.habittracker.ui.MainScreen
import com.example.habittracker.ui.PreferenceManager
import com.example.habittracker.ui.QuoteViewModel
import com.example.habittracker.ui.TutorialScreen
import com.example.habittracker.ui.theme.HabittrackerTheme
import java.util.Calendar
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val quotes = QuoteViewModel.getQuotesFromAssets(this) //Get quotes once per app launch
        val randomQuote = quotes.random() //Get random quote from list
        setContent {
            val context = LocalContext.current
            val showTutorial = remember { mutableStateOf(PreferenceManager.isFirstLaunch(context)) } //Show tutorial on first launch

            createNotificationChannel() //Create notification channel for daily reminder

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { //Request notification permission if android version is Tiramisu or higher
                requestNotificationPermission(this)
            }

            //Set up daily reminder alarm
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(this, HabitReminderReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val initialDelay = calculateInitialDelay() //Calculate delay for alarm until closest 12:45
            Log.d("MainActivity", "Initial delay for alarm: $initialDelay milliseconds")
            alarmManager.setRepeating( //Set alarm to repeat every 24 hours
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + initialDelay,
                TimeUnit.DAYS.toMillis(1),
                pendingIntent
            )

            LaunchedEffect(Unit) { //Set first launch to false after tutorial is finished
                if (showTutorial.value) {
                    PreferenceManager.setFirstLaunch(context, false)
                }
            }

            HabittrackerTheme {
                if (showTutorial.value) { //Show tutorial screen if first launch
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

    //Create notification channel for daily reminder
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

    //Calculate initial delay for alarm until closest 12:45, add 1 day if current time is after 12:45
    fun calculateInitialDelay(): Long {
        val currentTime = System.currentTimeMillis()
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 12)
            set(Calendar.MINUTE, 45)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        if (calendar.timeInMillis <= currentTime) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        return calendar.timeInMillis - currentTime
    }
}

//Request notification permission for android version Tiramisu or higher
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun requestNotificationPermission(activity: MainActivity) {
    requestPermissions(activity, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
}