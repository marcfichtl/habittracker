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
import java.util.Date

class DataViewModel (val repository: HabitRepository) : ViewModel() {
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

    fun markHabitsAsFinished(id: Int){
        viewModelScope.launch {
            val habit = repository.getHabitById(id).first()
            val updateFinished = habit.finished.toMutableList().apply { add(Date()) }
            repository.updateFinished(id, updateFinished)
        }
    }
}