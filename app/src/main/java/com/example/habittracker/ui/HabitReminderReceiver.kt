package com.example.habittracker.ui

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.habittracker.HabitApplication
import com.example.habittracker.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class HabitReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("HabitReminderReceiver", "onReceive called")
        val habitRepository = HabitApplication.instance.habitRepository
        val habitsFlow = habitRepository.getHabitsWithReminders()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val currentDayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)

        CoroutineScope(Dispatchers.IO).launch {
            habitsFlow.collect { habits ->
                habits.filter { it.reminder && (it.repeat == currentDayOfWeek || it.repeat == 0) }
                    .forEach { habit ->
                        val notification =
                            NotificationCompat.Builder(context, "HABIT_REMINDER_CHANNEL")
                                .setSmallIcon(R.drawable.calendaricon)
                                .setContentTitle("Habit Reminder")
                                .setContentText("Don't forget to ${habit.name}")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .build()

                        notificationManager.notify(habit.id, notification)
                    }
            }
        }
    }
}