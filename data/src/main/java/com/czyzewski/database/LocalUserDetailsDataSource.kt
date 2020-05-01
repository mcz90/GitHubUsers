package com.czyzewski.database

import com.czyzewski.database.dao.RequestDataDao
import com.czyzewski.database.dao.UserDetailsDao
import com.czyzewski.database.entity.UserDetailsEntity

class LocalUserDetailsDataSource(
    private val userDetailsDao: UserDetailsDao,
    private val requestDataDao: RequestDataDao
) {

    suspend fun getUser(userId: Long): UserDetailsEntity? {
        return userDetailsDao.getUser(userId)
    }

    suspend fun saveUser(entity: UserDetailsEntity): Long {
        return userDetailsDao.insert(entity)
    }
}