package com.czyzewski.usecase

import com.czyzewski.database.LocalUsersDataSource
import com.czyzewski.models.Repositories.Empty
import com.czyzewski.models.Repositories.Loading
import com.czyzewski.models.UserModel

class GetLocalUsersUseCase(private val localDataSource: LocalUsersDataSource) {

    suspend fun getUsers(query: String? = null): List<UserModel> {
        return localDataSource.getUsers(query)
            .map { entity ->
                UserModel(
                    id = entity.userId,
                    login = entity.login,
                    nodeId = entity.nodeId,
                    avatarUrl = entity.avatarUrl,
                    repositories = when (entity.repositories.repos.isEmpty()) {
                        true -> Empty
                        false -> Loading()
                    }
                )
            }
    }
}
