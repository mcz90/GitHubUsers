package com.czyzewski.database.typeconverters

import androidx.room.TypeConverter
import com.czyzewski.database.entity.RepositoryEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RepositoryConverter {

    @TypeConverter
    fun fromString(value: String): RepositoryEntity {
        val type = object : TypeToken<RepositoryEntity>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromRepository(entity: RepositoryEntity): String {
        return Gson().toJson(entity);
    }
}