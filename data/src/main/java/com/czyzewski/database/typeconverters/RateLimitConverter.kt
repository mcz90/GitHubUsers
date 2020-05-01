package com.czyzewski.database.typeconverters

import androidx.room.TypeConverter
import com.czyzewski.database.entity.RateLimitEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RateLimitConverter {

    @TypeConverter
    fun fromString(value: String): RateLimitEntity {
        val type = object : TypeToken<RateLimitEntity>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromStringMap(map: RateLimitEntity): String {
        return Gson().toJson(map)
    }
}