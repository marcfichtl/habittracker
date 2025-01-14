package com.example.habittracker.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.Date

@Entity(tableName = "habits")
@TypeConverters(Converters::class)
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val _id: Int=0,
    val name: String,
    val color: Int,
    val reminder: Boolean,
    val finished: List<Date> = emptyList()
)
