package com.czyzewski.usecase

import com.czyzewski.database.LocalUsersDataSource
import com.czyzewski.models.RepositoriesModel
import com.czyzewski.models.RepositoryModel

class GetLocalReposUseCase(private val localDataSource: LocalUsersDataSource) {

    suspend fun getRepos(userId: Long): RepositoriesModel {
        val result = localDataSource.getUsersRepos(userId)
        return RepositoriesModel(
            userId = result.userId,
            repositories = result.repos.map { repo ->
                RepositoryModel(
                    id = repo.repoId,
                    name = repo.name
                )
            }
        )
    }
}