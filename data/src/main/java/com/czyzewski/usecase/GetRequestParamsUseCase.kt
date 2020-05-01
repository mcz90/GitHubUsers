package com.czyzewski.usecase

import com.czyzewski.database.LocalUsersDataSource
import com.czyzewski.models.RequestQueryModel

class GetRequestParamsUseCase(private val localUsersDataSource: LocalUsersDataSource) {

    suspend fun getRequestParams(): RequestQueryModel {
        val entity = localUsersDataSource.getRequestParams()
        return RequestQueryModel(
            sinceParam = entity.sinceParam,
            allLoaded = entity.allLoaded
        )
    }
}

