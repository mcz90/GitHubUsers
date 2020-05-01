package com.czyzewski.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.czyzewski.database.entity.RepositoriesEntity
import com.czyzewski.database.entity.UserEntity

@Dao
interface UsersDao {

    @Query("SELECT * FROM users")
    suspend fun loadAll(): List<UserEntity>

    @Query("SELECT * FROM users WHERE login LIKE :query OR repositories LIKE :query")
    suspend fun loadFiltered(query: String): List<UserEntity>

    @Query("SELECT * FROM users WHERE userId=:userId")
    suspend fun getUser(userId: Long): UserEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entities: List<UserEntity>): List<Long>

    @Query("UPDATE users SET repositories=:entity, shouldSync=0 WHERE userId=:userId")
    suspend fun saveRepositories(userId: Long, entity: RepositoriesEntity)

    @Query("SELECT repositories FROM users WHERE userId=:userId")
    suspend fun getUsersRepos(userId: Long): RepositoriesEntity
}
