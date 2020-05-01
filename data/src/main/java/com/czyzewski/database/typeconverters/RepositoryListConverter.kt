package com.czyzewski.database.typeconverters

import androidx.room.TypeConverter
import com.czyzewski.database.entity.RepositoryEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RepositoryListConverter {

    @TypeConverter
    fun fromString(value: String): List<RepositoryEntity> {
        val type = object : TypeToken<List<RepositoryEntity>>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromRepository(entity: List<RepositoryEntity>): String {
        return Gson().toJson(entity)
    }
}