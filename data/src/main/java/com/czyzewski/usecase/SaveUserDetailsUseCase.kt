package com.czyzewski.usecase

import com.czyzewski.database.LocalUserDetailsDataSource
import com.czyzewski.models.UserDetailsModel

class SaveUserDetailsUseCase(private val localDataSource: LocalUserDetailsDataSource) {

    suspend fun saveUser(userModel: UserDetailsModel) {
        localDataSource.saveUser(userModel.toEntity())
    }
}
