package com.czyzewski.database.typeconverters

import androidx.room.TypeConverter
import com.czyzewski.database.entity.RepositoriesEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RepositoriesConverter {

    @TypeConverter
    fun fromString(value: String?): RepositoriesEntity {
        val type = object : TypeToken<RepositoriesEntity>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromRepositories(entities: RepositoriesEntity): String? {
        return Gson().toJson(entities)
    }
}