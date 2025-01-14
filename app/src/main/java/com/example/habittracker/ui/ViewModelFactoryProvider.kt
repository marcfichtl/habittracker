package com.example.habittracker.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.habittracker.HabitApplication

object ViewModelFactoryProvider {
    val Factory = viewModelFactory {
        initializer {
            val application = this[APPLICATION_KEY] as HabitApplication
            DataViewModel(application.habitRepository)
        }
        initializer {
            QuoteViewModel()
        }
    }
}