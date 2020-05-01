package com.czyzewski.usecase

import com.czyzewski.models.UserModel
import com.czyzewski.usecase.GetStoredUsersUseCase.StoredUsersData.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetStoredUsersUseCase(private val getLocalUsersUseCase: GetLocalUsersUseCase) {

    sealed class StoredUsersData {
        data class Success(val users: List<UserModel>) : StoredUsersData()
        data class Error(val cause: Throwable) : StoredUsersData()
        object Loading : StoredUsersData()
    }

    @ExperimentalCoroutinesApi
    suspend fun get(query: String?): Flow<StoredUsersData> {
        return flow<StoredUsersData> {
            val localUsers = getLocalUsersUseCase.getUsers(query)
                emit(Success(localUsers))
        }
    }
}
