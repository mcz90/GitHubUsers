package com.czyzewski.models

import com.czyzewski.mappers.ErrorModel
import java.util.*

sealed class UsersResponse {
    object Loading : UsersResponse()
    data class Loaded(val headerDataModel: HeaderDataModel, val data: List<UserModel>) : UsersResponse()
    object MoreLoading : UsersResponse()
    object AllLoaded : UsersResponse()
    data class Empty(val headerDataModel: HeaderDataModel) : UsersResponse()
    data class Error(val headerDataModel: HeaderDataModel, val error: ErrorModel) : UsersResponse()
}

sealed class RepositoriesResponse {
    data class Loading(val userId: Long) : RepositoriesResponse()
    data class Loaded(val userId: Long, val headerDataModel: HeaderDataModel, val repositories: RepositoriesModel) : RepositoriesResponse()
    data class Empty( val userId: Long, val headerDataModel: HeaderDataModel) : RepositoriesResponse()
    data class Error(val userId: Long, val headerDataModel: HeaderDataModel) : RepositoriesResponse()
    data class Syncing(val userId: Long) : RepositoriesResponse()
    data class SyncSuccess(val userId: Long, val headerDataModel: HeaderDataModel, val repositories: RepositoriesModel) : RepositoriesResponse()
    data class SyncError(val userId: Long, val headerDataModel: HeaderDataModel?) : RepositoriesResponse()
}

sealed class HeaderDataModel {
    data class DataModel(
        val requestsLimit: Int,
        val remainingRequest: Int,
        val timeUntilReset: Date,
        val nextSinceParam: SinceParam
    ) : HeaderDataModel()

    object Ignored : HeaderDataModel()
}

sealed class SinceParam {
    data class Next(val sinceParam: Long) : SinceParam()
    object AllLoaded : SinceParam()
    object Ignored : SinceParam()
}