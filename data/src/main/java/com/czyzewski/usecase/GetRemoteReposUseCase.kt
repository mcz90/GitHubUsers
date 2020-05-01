package com.czyzewski.usecase

import com.czyzewski.models.RepositoriesModel
import com.czyzewski.models.RepositoryModel
import com.czyzewski.net.RemoteUsersDataSource
import com.czyzewski.models.ResponseModel
import com.czyzewski.net.toModel

class GetRemoteReposUseCase(private val remoteDataSource: RemoteUsersDataSource) {

    suspend fun getRepos(
        userId: Long,
        userName: String,
        type: String,
        perPage: Int
    ): ResponseModel<RepositoriesModel> {
        return remoteDataSource.getUsersRepos(userName, type, perPage).toModel { dtos ->
            RepositoriesModel(
                userId = userId,
                repositories = dtos.map {
                    RepositoryModel(
                        id = it.id,
                        name = it.name
                    )
                }
            )
        }
    }
}