package com.czyzewski.net

import com.czyzewski.net.dto.RepositoryDto
import com.czyzewski.net.dto.UserDto
import retrofit2.Response
import retrofit2.http.*

interface UsersApiService {
    @GET("/users")
    suspend fun getUsers(@Query("since") since: Long): Response<List<UserDto>>

    @GET("/users/{userName}/repos")
    suspend fun getUsersRepositories(
        @Path("userName") userName: String,
        @Query("type") type: String,
        @Query("per_page") perPage: Int,
        @Query("limit") limit: Int? = 2
    ): Response<List<RepositoryDto>>
}
