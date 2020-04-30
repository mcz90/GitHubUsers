package com.czyzewski.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.czyzewski.database.entity.RepositoryEntity
import com.czyzewski.database.entity.UserEntity

@Dao
interface UsersDao {

    @Query("SELECT * FROM users")
    suspend fun loadAll(): List<UserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entities: List<UserEntity>): List<Long>

    @Query("UPDATE users SET repositories=:repositories, shouldSync=0 WHERE id=:id")
    suspend fun saveRepositories(id: Long, repositories: List<RepositoryEntity>)
}