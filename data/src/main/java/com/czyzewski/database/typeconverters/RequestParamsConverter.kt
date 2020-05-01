package com.czyzewski.database.typeconverters

import androidx.room.TypeConverter
import com.czyzewski.database.entity.RequestParamsEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RequestParamsConverter {

    @TypeConverter
    fun fromString(value: String): RequestParamsEntity {
        val type = object : TypeToken<RequestParamsEntity>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromStringMap(map: RequestParamsEntity): String {
        return Gson().toJson(map)
    }
}

