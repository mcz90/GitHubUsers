package com.czyzewski.usecase

import com.czyzewski.models.UserDetailsModel
import com.czyzewski.usecase.GetUserDetailsUseCase.UsersDetailsData.Success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetUserDetailsUseCase(
    private val getLocalUserDetailsUseCase: GetLocalUserDetailsUseCase,
    private val getRemoteUserDetailsUseCase: GetRemoteUserDetailsUseCase,
    private val saveUserDetailsUseCase: SaveUserDetailsUseCase
) {
    sealed class UsersDetailsData {
        data class Success(val userDetails: UserDetailsModel) : UsersDetailsData()
        data class Error(val cause: Throwable) : UsersDetailsData()
        object Loading : UsersDetailsData()
    }

    suspend fun getUserDetails(userId: Long, userName: String): Flow<UsersDetailsData> {
        return flow<UsersDetailsData> {
            val localUser = getLocalUserDetailsUseCase.getUser(userId)
            emit(
                Success(
                    if (localUser != null) {
                        localUser
                    } else {
                        val responseModel = getRemoteUserDetailsUseCase.getUserDetails(userName)
                            .also { saveUserDetailsUseCase.saveUser(it.body) }.body
                        responseModel
                    }
                )
            )
        }
    }
}