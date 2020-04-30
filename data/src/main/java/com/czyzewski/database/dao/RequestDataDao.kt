package com.czyzewski.database.dao

import androidx.room.*
import com.czyzewski.database.entity.RequestDataEntity

@Dao
interface RequestDataDao {
    @Query("SELECT * FROM requestData")
    suspend fun get(): RequestDataEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entities: RequestDataEntity)

    @Query("UPDATE requestData SET allLoaded=:allLoaded")
    suspend fun updateAllLoadedParam(allLoaded: Boolean)

    @Query("UPDATE requestData SET nextRemainingParam=:since")
    suspend fun updateNextRemainingParam(since: Long)

    @Transaction
    suspend fun getRequestData(): RequestDataEntity {
        if (get() == null) {
            insert(RequestDataEntity())
        }
        return get()!!
    }
}