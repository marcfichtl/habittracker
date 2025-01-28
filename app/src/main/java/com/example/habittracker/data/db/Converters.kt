package com.example.habittracker.data.db

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//Converters to convert the finished dates to a string and vice versa
class Converters {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    @TypeConverter
    fun fromDateList(dates: List<Date>?): String? {
        return dates?.joinToString(separator = ",") { dateFormat.format(it) }
    }

    @TypeConverter
    fun toDateList(dateString: String?): List<Date>? {
        return dateString?.split(",")?.mapNotNull {
            if (it.isNotEmpty()) dateFormat.parse(it) else null
        }
    }
}