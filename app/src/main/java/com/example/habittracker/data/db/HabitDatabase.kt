package com.example.habittracker.data.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.habittracker.HabitApplication

@Database(entities = [HabitEntity::class], version = 1)

abstract class HabitDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao

    companion object {
        @Volatile
        private var Instance: HabitDatabase? = null

        fun getDatabase(context: HabitApplication): HabitDatabase {
            return Instance ?: synchronized(this) {
                val instance = Room.databaseBuilder(context, HabitDatabase::class.java, "habit_database")
                    .fallbackToDestructiveMigration()
                    .build()
                Instance = instance
                return instance
            }
        }
    }
}