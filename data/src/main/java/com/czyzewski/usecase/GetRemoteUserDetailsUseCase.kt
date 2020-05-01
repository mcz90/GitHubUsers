package com.czyzewski.usecase

import com.czyzewski.models.ResponseModel
import com.czyzewski.models.UserDetailsModel
import com.czyzewski.net.RemoteUsersDataSource
import com.czyzewski.net.toModel

class GetRemoteUserDetailsUseCase(private val remoteDataSource: RemoteUsersDataSource) {

    suspend fun getUserDetails(userName: String): ResponseModel<UserDetailsModel> {
        return remoteDataSource.getUserDetails(userName)
            .toModel { it.toModel() }
    }
}
