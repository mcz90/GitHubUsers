package com.czyzewski.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.czyzewski.database.dao.RateLimitDao
import com.czyzewski.database.dao.RequestDataDao
import com.czyzewski.database.dao.UsersDao
import com.czyzewski.database.dao.UserDetailsDao
import com.czyzewski.database.entity.*
import com.czyzewski.database.typeconverters.*

@Database(
    entities = [
        UserEntity::class,
        UserDetailsEntity::class,
        RepositoryEntity::class,
        RepositoriesEntity::class,
        RateLimitEntity::class,
        RequestParamsEntity::class], version = 1
)
@TypeConverters(
    DateConverter::class,
    RequestParamsConverter::class,
    RateLimitConverter::class,
    StringMapConverter::class,
    StringListConverter::class,
    UserConverter::class,
    UserDetailsConverter::class,
    RepositoryListConverter::class,
    RepositoryConverter::class,
    RepositoriesConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usersDao(): UsersDao
    abstract fun userDetailsDao(): UserDetailsDao
    abstract fun requestDataDao(): RequestDataDao
    abstract fun rateLimitDao(): RateLimitDao
}





