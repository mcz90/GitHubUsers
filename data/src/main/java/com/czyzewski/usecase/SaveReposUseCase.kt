package com.czyzewski.usecase

import com.czyzewski.database.LocalUsersDataSource
import com.czyzewski.database.entity.RepositoriesEntity
import com.czyzewski.database.entity.RepositoryEntity
import com.czyzewski.models.RepositoriesModel

class SaveReposUseCase(private val localDataSource: LocalUsersDataSource) {

    suspend fun saveRepos(repos: RepositoriesModel) {
        localDataSource.saveRepositories(
            RepositoriesEntity(
                userId = repos.userId,
                repos = repos.repositories.map { repo ->
                    RepositoryEntity(
                        repoId = repo.id,
                        name = repo.name
                    )
                }
            ))
    }
}