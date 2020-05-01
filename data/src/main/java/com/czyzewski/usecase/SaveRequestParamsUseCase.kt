package com.czyzewski.usecase

import com.czyzewski.database.LocalUsersDataSource
import com.czyzewski.models.RequestQueryModel

class SaveRequestParamsUseCase(private val localUsersDataSource: LocalUsersDataSource) {

    suspend fun saveRequestParams(headerData: RequestQueryModel) {
        localUsersDataSource.insertRequestParams(headerData)
    }
}