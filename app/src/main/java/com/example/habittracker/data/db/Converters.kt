package com.example.habittracker.data.db

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Converters {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    /*@TypeConverter
    fun fromString(value: String): List<String> {
        return if (value.isEmpty()) emptyList() else value.split(",").map { it.trim() }
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return list.joinToString(",")
    }*/

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