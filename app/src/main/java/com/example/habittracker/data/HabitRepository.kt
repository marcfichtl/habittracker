package com.example.habittracker.data

import com.example.habittracker.data.db.HabitDao
import com.example.habittracker.data.db.HabitEntity
import kotlinx.coroutines.flow.map

class HabitRepository (private val habitDao: HabitDao) {

    val habits = habitDao.getAllHabits().map { list ->
        list.map { habitEntity ->
            Habit(
                habitEntity._id,
                habitEntity.name,
                habitEntity.color,
                habitEntity.reminder,
                habitEntity.repeat,
                habitEntity.finished
            )
        }
    }

    suspend fun addHabit(habit: Habit) {
        val entity = HabitEntity(
            habit.id,
            habit.name,
            habit.color,
            habit.reminder,
            habit.repeat,
            habit.finished
        )
        habitDao.addHabit(entity)
    }

    suspend fun removeHabit(id: Int) {
        habitDao.deleteHabitById(id)
    }

    suspend fun updateHabit(habit: Habit) {
        val entity = HabitEntity(
            habit.id,
            habit.name,
            habit.color,
            habit.reminder,
            habit.repeat,
            habit.finished
        )
        habitDao.updateHabit(entity)
    }

    fun getHabitById(id: Int) = habitDao.getHabitById(id).map { habitEntity ->
        Habit(
            habitEntity._id,
            habitEntity.name,
            habitEntity.color,
            habitEntity.reminder,
            habitEntity.repeat,
            habitEntity.finished
        )
    }

}

