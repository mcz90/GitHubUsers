package com.czyzewski.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.czyzewski.database.entity.UserDetailsEntity

@Dao
interface UserDetailsDao {

    @Query("SELECT * FROM user_details WHERE id=:userId")
    suspend fun getUser(userId: Long): UserDetailsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: UserDetailsEntity): Long

}