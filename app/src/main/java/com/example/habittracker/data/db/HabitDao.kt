package com.example.habittracker.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface HabitDao {

    @Insert
    suspend fun addHabit(habit: HabitEntity)

    @Update
    suspend fun updateHabit(habit: HabitEntity)

    @Delete
    suspend fun deleteHabit(habit: HabitEntity)

    @Query ("SELECT * FROM habits")
    fun getAllHabits(): Flow<List<HabitEntity>>

    @Query ("DELETE FROM habits WHERE _id = :id")
    suspend fun deleteHabitById(id: Int)

    @Query ("SELECT * FROM habits WHERE _id = :id")
    fun getHabitById(id: Int): Flow<HabitEntity>

    @Query ("UPDATE habits SET finished = :finished WHERE _id = :id")
    suspend fun updateFinished(id: Int, finished: List<Date>)
}