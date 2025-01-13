package com.example.habittracker.data

data class Habit (
    val id: Int,
    val name: String,
    val color: Int,
    val reminder: Boolean,
    val repeat: List<String>
)