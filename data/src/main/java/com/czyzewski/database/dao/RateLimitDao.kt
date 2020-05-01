package com.czyzewski.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.czyzewski.database.entity.RateLimitEntity

@Dao
interface RateLimitDao {

    @Query("SELECT * FROM rateLimit")
    suspend fun get(): RateLimitEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: RateLimitEntity)

    @Transaction
    suspend fun getRequestParams(): RateLimitEntity {
        if (get() == null) {
            insert(RateLimitEntity())
        }
        return get()!!
    }
}