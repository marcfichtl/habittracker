package com.example.habittracker.data

import java.util.Date

data class Habit (
    val id: Int,
    val name: String,
    val color: Int,
    val reminder: Boolean,
    val finished: List<Date>
)