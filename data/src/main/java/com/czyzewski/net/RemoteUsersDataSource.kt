package com.czyzewski.net

import com.czyzewski.models.ResponseModel
import com.czyzewski.net.dto.RepositoryDto
import com.czyzewski.net.dto.UserDetailsDto
import com.czyzewski.net.dto.UserDto

class RemoteUsersDataSource(private val usersApiService: UsersApiService) {

    suspend fun getUsers(since: Long): ResponseModel<List<UserDto>> {
        return usersApiService.getUsers(since).handle()
    }

    suspend fun getUserDetails(userName: String): ResponseModel<UserDetailsDto> {
        return usersApiService.getUserDetails(userName).handle()
    }

    suspend fun getUsersRepos(
        userName: String,
        type: String,
        perPage: Int
    ): ResponseModel<List<RepositoryDto>> {
        return usersApiService.getUsersRepositories(userName, type, perPage).handle()
    }
}