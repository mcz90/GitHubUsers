package com.czyzewski.database.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class DateConverter {
    @TypeConverter
    fun fromString(value: String): Date {
        val type = object : TypeToken<Date>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromStringMap(map: Date): String {
        return Gson().toJson(map)
    }
}