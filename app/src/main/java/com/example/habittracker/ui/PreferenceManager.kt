package com.example.habittracker.ui

import android.content.Context
import android.content.SharedPreferences

//Used for storing first launch preference
object PreferenceManager {
    private const val PREF_NAME = "app_preferences"
    private const val KEY_FIRST_LAUNCH = "first_launch"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun isFirstLaunch(context: Context): Boolean {
        return getPreferences(context).getBoolean(KEY_FIRST_LAUNCH, true)
    }

    fun setFirstLaunch(context: Context, isFirstLaunch: Boolean) {
        getPreferences(context).edit().putBoolean(KEY_FIRST_LAUNCH, isFirstLaunch).apply()
    }
}