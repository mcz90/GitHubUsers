package com.czyzewski.database

import com.czyzewski.database.dao.RequestDataDao
import com.czyzewski.database.dao.UsersDao
import com.czyzewski.database.entity.RepositoriesEntity
import com.czyzewski.database.entity.RequestParamsEntity
import com.czyzewski.database.entity.UserEntity
import com.czyzewski.models.RequestQueryModel

class LocalUsersDataSource(
    private val usersDao: UsersDao,
    private val requestDataDao: RequestDataDao
) {

    suspend fun getUsers(query: String?): List<UserEntity> {
        return query
            ?.takeIf { it.isNotBlank() }
            ?.let { usersDao.loadFiltered("%$it%") }
            ?: usersDao.loadAll()
    }

    suspend fun getUser(userId: Long): UserEntity {
        return usersDao.getUser(userId)
    }

    suspend fun saveData(entities: List<UserEntity>): List<Long> {
        return usersDao.insert(entities)
    }

    suspend fun insertRequestParams(headerData: RequestQueryModel) {
        val current = getRequestParams()
        return requestDataDao.insertRequestParams(
            RequestParamsEntity(
                sinceParam = when {
                    headerData.sinceParam != null -> headerData.sinceParam
                    else -> current.sinceParam
                },
                allLoaded = when (val allLoaded = headerData.allLoaded) {
                    allLoaded == true -> allLoaded
                    else -> current.allLoaded
                }
            )
        )
    }

    suspend fun getRequestParams(): RequestParamsEntity {
        return requestDataDao.getRequestParams()
    }

    suspend fun getUsersRepos(userId: Long): RepositoriesEntity {
        return usersDao.getUsersRepos(userId)
    }

    suspend fun saveRepositories(entity: RepositoriesEntity) {
        return usersDao.saveRepositories(entity.userId, entity)
    }
}
