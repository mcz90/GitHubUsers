package com.czyzewski.usecase

import com.czyzewski.database.LocalUserDetailsDataSource
import com.czyzewski.models.UserDetailsModel

class GetLocalUserDetailsUseCase(private val localDataSource: LocalUserDetailsDataSource) {

    suspend fun getUser(userId: Long): UserDetailsModel? {
        return localDataSource.getUser(userId)?.toModel()
    }
}
