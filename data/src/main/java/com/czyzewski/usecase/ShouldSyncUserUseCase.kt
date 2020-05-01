package com.czyzewski.usecase

import com.czyzewski.database.LocalUsersDataSource

class ShouldSyncUserUseCase(private val localUsersDataSource: LocalUsersDataSource) {

    suspend fun get(userId: Long): Boolean {
        return localUsersDataSource.getUser(userId).shouldSync
    }

}
