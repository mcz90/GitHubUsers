package com.czyzewski.database.typeconverters

import androidx.room.TypeConverter
import com.czyzewski.database.entity.UserEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UserConverter {

    @TypeConverter
    fun fromString(value: String): UserEntity {
        val type = object : TypeToken<UserEntity>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromStringMap(entity: UserEntity): String {
        return Gson().toJson(entity)
    }
}