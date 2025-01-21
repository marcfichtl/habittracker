package com.example.habittracker

import android.app.Application
import com.example.habittracker.data.HabitRepository
import com.example.habittracker.data.db.HabitDatabase

class HabitApplication : Application() {
    companion object {
        lateinit var instance: HabitApplication
            private set
    }

    val habitRepository by lazy {
        val dao = HabitDatabase.getDatabase(this).habitDao()

        HabitRepository(dao)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}