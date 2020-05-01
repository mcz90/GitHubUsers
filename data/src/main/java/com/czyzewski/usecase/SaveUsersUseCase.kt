package com.czyzewski.usecase

import com.czyzewski.database.LocalUsersDataSource
import com.czyzewski.database.entity.RepositoriesEntity
import com.czyzewski.database.entity.UserEntity
import com.czyzewski.models.UserModel

class SaveUsersUseCase(private val localDataSource: LocalUsersDataSource) {
    suspend fun saveUsers(users: List<UserModel>) {
        localDataSource.saveData(users.map {
            UserEntity(
                userId = it.id,
                login = it.login,
                nodeId = it.nodeId,
                avatarUrl = it.avatarUrl,
                shouldSync = true,
                repositories = RepositoriesEntity(it.id, emptyList())
            )
        })
    }
}