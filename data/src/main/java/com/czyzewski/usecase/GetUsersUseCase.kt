package com.czyzewski.usecase

import com.czyzewski.models.UserModel
import com.czyzewski.models.RequestQueryModel
import com.czyzewski.usecase.GetUsersUseCase.UsersData.Success
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetUsersUseCase(
    private val getLocalUsersUseCase: GetLocalUsersUseCase,
    private val getRemoteUsersUseCase: GetRemoteUsersUseCase,
    private val saveUsersUseCase: SaveUsersUseCase,
    private val getRequestParamsUseCase: GetRequestParamsUseCase,
    private val saveRequestParamsUseCase: SaveRequestParamsUseCase
) {

    sealed class UsersData {
        var requestQueryModel: RequestQueryModel? = null

        data class Success(val users: List<UserModel>) : UsersData()
        data class Error(val cause: Throwable) : UsersData()
        data class Loading(val first: Boolean) : UsersData()
    }

    @ExperimentalCoroutinesApi
    suspend fun getUsers(first: Boolean): Flow<UsersData> {
        return flow<UsersData> {
            val sinceParam = getRequestParamsUseCase.getRequestParams().sinceParam
            val localUsers = getLocalUsersUseCase.getUsers()
            requireNotNull(sinceParam)
            if (localUsers.isEmpty() || !first) {
                val responseModel = getRemoteUsersUseCase.getUsers(sinceParam)
                    .also {
                        saveRequestParamsUseCase.saveRequestParams(it.requestQuery)
                        saveUsersUseCase.saveUsers(it.body)
                    }.body
                emit(
                    Success(responseModel)
                        .apply { requestQueryModel = getRequestParamsUseCase.getRequestParams() })
            } else {
                emit(
                    Success(localUsers)
                        .apply { requestQueryModel = getRequestParamsUseCase.getRequestParams() })
            }
        }
    }
}
