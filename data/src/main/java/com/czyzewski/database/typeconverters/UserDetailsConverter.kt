package com.czyzewski.database.typeconverters

import androidx.room.TypeConverter
import com.czyzewski.database.entity.UserDetailsEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UserDetailsConverter {

    @TypeConverter
    fun fromString(value: String): UserDetailsEntity {
        val type = object : TypeToken<UserDetailsEntity>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromStringMap(entity: UserDetailsEntity): String {
        return Gson().toJson(entity)
    }
}