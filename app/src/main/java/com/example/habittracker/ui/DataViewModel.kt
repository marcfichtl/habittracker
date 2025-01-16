package com.example.habittracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittracker.data.Habit
import com.example.habittracker.data.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DataViewModel(val repository: HabitRepository) : ViewModel() {
    private val _habitsUiState = MutableStateFlow(HabitsUiState(emptyList()))
    val habitsUiState = _habitsUiState

    init {
        viewModelScope.launch {
            repository.habits.collect { data ->
                _habitsUiState.update { oldState ->
                    oldState.copy(
                        habits = data
                    )
                }
            }
        }
    }

    fun onAddHabitClick(habit: Habit) {
        viewModelScope.launch {
            repository.addHabit(habit)
        }
    }

    fun onDeleteHabitClick(id: Int) {
        viewModelScope.launch {
            repository.removeHabit(id)
        }
    }

    fun getHabitById(id: Int): Flow<Habit> {
        return repository.getHabitById(id)
    }

    fun updateHabit(habit: Habit) {
        viewModelScope.launch {
            repository.updateHabit(habit)
        }
    }

    fun markHabitsAsFinished(habitId: Int) {
        viewModelScope.launch {
            val habit = repository.getHabitById(habitId).first()
            val updatedFinishedDates = habit.finished.toMutableList().apply {
                add(Date())
            }
            repository.updateHabit(habit.copy(finished = updatedFinishedDates))
        }
    }

    fun unmarkHabitAsFinished(habitId: Int) {
        viewModelScope.launch {
            val habit = repository.getHabitById(habitId).first()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val todayString = dateFormat.format(Date())
            val updatedFinishedDates =
                habit.finished.filter { dateFormat.format(it) != todayString }
            repository.updateHabit(habit.copy(finished = updatedFinishedDates))
        }
    }
}