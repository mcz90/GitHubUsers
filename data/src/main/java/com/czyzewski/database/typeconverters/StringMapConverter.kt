package com.czyzewski.database.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StringMapConverter {

    @TypeConverter
    fun fromString(value: String): Map<String, String> {
        val type = object : TypeToken<Map<String, String>>() {}.type
        return Gson().fromJson(value, type);
    }

    @TypeConverter
    fun fromStringMap(map: Map<String, String>): String {
        return Gson().toJson(map);
    }
}