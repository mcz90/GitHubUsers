package com.czyzewski.net

import com.czyzewski.net.dto.RepositoryDto
import com.czyzewski.net.dto.UserDetailsDto
import com.czyzewski.net.dto.UserDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UsersApiService {
    @GET("/users")
    suspend fun getUsers(@Query("since") since: Long): Response<List<UserDto>>

    @GET("/users/{userName}")
    suspend fun getUserDetails(@Path("userName") userName: String): Response<UserDetailsDto>

    @GET("/users/{userName}/repos")
    suspend fun getUsersRepositories(
        @Path("userName") userName: String,
        @Query("type") type: String,
        @Query("per_page") perPage: Int,
        @Query("limit") limit: Int? = 1
    ): Response<List<RepositoryDto>>
}
