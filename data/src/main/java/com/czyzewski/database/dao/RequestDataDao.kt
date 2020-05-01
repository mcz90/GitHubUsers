package com.czyzewski.database.dao

import androidx.room.*
import com.czyzewski.database.entity.RequestParamsEntity

@Dao
interface RequestDataDao {
    @Query("SELECT * FROM requestData")
    suspend fun get(): RequestParamsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entities: RequestParamsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequestParams(entity: RequestParamsEntity)

    @Transaction
    suspend fun getRequestParams(): RequestParamsEntity {
        if (get() == null) {
            insert(RequestParamsEntity())
        }
        return get()!!
    }
}