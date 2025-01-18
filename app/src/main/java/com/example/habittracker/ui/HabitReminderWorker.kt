package com.example.habittracker.ui

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.habittracker.R
import com.example.habittracker.data.HabitRepository
import com.example.habittracker.data.db.HabitDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HabitReminderWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        sendNotification("Keep up the good work!")

        return@withContext Result.success()
    }

    private fun sendNotification(message: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification: Notification = NotificationCompat.Builder(applicationContext, "HABIT_REMINDER_CHANNEL")
            .setContentTitle("Don't forget to check in on your habits!")
            .setContentText(message)
            .setSmallIcon(R.drawable.calendaricon)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        notificationManager.notify(1, notification)
    }
}