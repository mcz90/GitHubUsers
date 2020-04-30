package com.czyzewski.githubuserslist.usevase

import com.czyzewski.models.UsersResponse
import com.czyzewski.repository.UsersRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart

class GetUsersUserCase(private val repository: UsersRepository) {

    @ExperimentalCoroutinesApi
    suspend fun getUsers(first: Boolean): Flow<UsersResponse> {
        return repository.getUsers(first)
            .onStart {
                if (first) UsersResponse.Loading
                else UsersResponse.MoreLoading
            }
    }
}