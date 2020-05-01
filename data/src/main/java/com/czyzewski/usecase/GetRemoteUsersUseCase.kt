package com.czyzewski.usecase

import com.czyzewski.models.Repositories
import com.czyzewski.models.UserModel
import com.czyzewski.net.RemoteUsersDataSource
import com.czyzewski.models.ResponseModel
import com.czyzewski.net.toModel

class GetRemoteUsersUseCase(private val remoteDataSource: RemoteUsersDataSource) {

    suspend fun getUsers(since: Long): ResponseModel<List<UserModel>> {
        return remoteDataSource.getUsers(since)
            .toModel { dtos ->
                dtos.map { dto ->
                    UserModel(
                        id = dto.id,
                        login = dto.login,
                        nodeId = dto.nodeId,
                        avatarUrl = dto.avatarUrl,
                        repositories = Repositories.Loading()
                    )
                }
            }
    }
}
