package com.czyzewski.database

import com.czyzewski.database.dao.RequestDataDao
import com.czyzewski.database.dao.UsersDao
import com.czyzewski.database.entity.RepositoryEntity
import com.czyzewski.database.entity.RequestDataEntity
import com.czyzewski.database.entity.UserEntity

class LocalUsersDataSource(
    private val usersDao: UsersDao,
    private val requestDataDao: RequestDataDao
) {

    suspend fun getUsers(): List<UserEntity> {
        return usersDao.loadAll()
    }

    suspend fun saveData(entities: List<UserEntity>): List<Long> {
        return usersDao.insert(entities)
    }

    suspend fun updateNextRemainingParam(since: Long) {
        return requestDataDao.updateNextRemainingParam(since)
    }

    suspend fun updateAllLoadedParam(allLoaded: Boolean) {
        return requestDataDao.updateAllLoadedParam(allLoaded)
    }

    suspend fun getRequestData(): RequestDataEntity {
        return requestDataDao.getRequestData()
    }

   suspend fun saveRepositories(id: Long, entities: List<RepositoryEntity>) {
        return usersDao.saveRepositories(id, entities)
    }
}