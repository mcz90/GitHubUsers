package com.czyzewski.net

import com.czyzewski.net.dto.RepositoryDto
import com.czyzewski.net.dto.UserDto
import retrofit2.Response

class RemoteUsersDataSource(private val usersApiService: UsersApiService) {

    suspend fun getUsers(since: Long): Response<List<UserDto>> {
        return usersApiService.getUsers(since)
    }

    suspend fun getUsersRepos(
        userName: String,
        type: String,
        perPage: Int
    ): Response<List<RepositoryDto>> {
        return usersApiService.getUsersRepositories(userName, type, perPage)
    }
}