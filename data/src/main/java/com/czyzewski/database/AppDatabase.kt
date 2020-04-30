package com.czyzewski.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.czyzewski.database.dao.RequestDataDao
import com.czyzewski.database.dao.UsersDao
import com.czyzewski.database.entity.RepositoryEntity
import com.czyzewski.database.entity.RequestDataEntity
import com.czyzewski.database.entity.UserEntity
import com.czyzewski.database.typeconverters.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

@Database(entities = [UserEntity::class, RepositoryEntity::class, RequestDataEntity::class], version = 1)
@TypeConverters(
    StringMapConverter::class,
    StringListConverter::class,
    UserConverter::class,
    RepositoryListConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usersDao(): UsersDao
    abstract fun requestDataDao(): RequestDataDao
}





