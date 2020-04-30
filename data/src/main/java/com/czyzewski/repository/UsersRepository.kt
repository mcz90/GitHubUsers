package com.czyzewski.repository

import com.czyzewski.database.LocalUsersDataSource
import com.czyzewski.database.entity.RepositoryEntity
import com.czyzewski.mappers.ErrorModel.ApiError
import com.czyzewski.mappers.IUserMapper
import com.czyzewski.models.*
import com.czyzewski.models.SinceParam.Ignored
import com.czyzewski.models.SinceParam.Next
import com.czyzewski.net.RemoteUsersDataSource
import com.czyzewski.net.dto.RepositoryDto
import com.czyzewski.net.dto.UserDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.Headers
import retrofit2.Response
import java.util.*


class UsersRepository(
    private val localDataSource: LocalUsersDataSource,
    private val remoteDataSource: RemoteUsersDataSource,
    private val mapper: IUserMapper
) {

    suspend fun getUsers(first: Boolean): Flow<UsersResponse> {
        return flow {
            val localData = localDataSource.getUsers()
            emit(
                when {
                    localData.isEmpty() || !first -> getRemote()
                    else -> UsersResponse.Loaded(
                        HeaderDataModel.Ignored,
                        mapper.entitiesToModels(localData)
                    )
                }
            )
        }
    }

    private suspend fun getRemote(): UsersResponse {
        val requestData = localDataSource.getRequestData()
        if (requestData.allLoaded) {
            return (UsersResponse.AllLoaded)
        }
        val response = remoteDataSource.getUsers(requestData.nextRemainingParam)
        val requestModel = mapHeader(response.headers()) as HeaderDataModel.DataModel
        when (requestModel.nextSinceParam) {
            is Next -> localDataSource.updateNextRemainingParam(requestModel.nextSinceParam.sinceParam)
            SinceParam.AllLoaded -> localDataSource.updateAllLoadedParam(true)
        }
        response
            .takeIf { it.isSuccessful && it.body() != null }
            ?.let {
                val data = it.body()!!
                return if (data.isNotEmpty()) {
                    synchronizeWithDatabase(data)
                    UsersResponse.Loaded(
                        requestModel,
                        data.map { dto -> mapper.dtoToModel(dto) })
                } else {
                    UsersResponse.Empty(requestModel)
                }
            } ?: return UsersResponse.Error(
            requestModel,
            ApiError(
                code = response.code(),
                message = response.message(),
                body = response.errorBody()
            )
        )
    }


    suspend fun getUserRepos(
        userId: Long,
        userName: String,
        type: String,
        perPage: Int
    ): Flow<RepositoriesResponse> {
        return flow {
            val savedUser = localDataSource.getUsers().first { it.id == userId }
            if (!savedUser.shouldSync) {
                emit(
                    savedUser.repositories.takeIf { it.isEmpty() }?.let {
                        RepositoriesResponse.Empty(userId, HeaderDataModel.Ignored)
                    } ?: RepositoriesResponse.Loaded(
                        userId,
                        HeaderDataModel.Ignored,
                        RepositoriesModel(userId, savedUser.repositories.map { it.name })
                    )
                )
            } else {
                val reposResponse = remoteDataSource.getUsersRepos(userName, type, perPage)
                val requestModel = mapRepoHeader(reposResponse.headers())
                emit(reposResponse
                    .takeIf { it.isSuccessful && it.body() != null }
                    ?.let { response ->
                        val data = response.body() as List<RepositoryDto>
                        synchronizeRepositories(userId, data)
                        data
                            .takeIf { it.isNotEmpty() }
                            ?.let {
                                RepositoriesResponse.Loaded(
                                    userId,
                                    requestModel,
                                    RepositoriesModel(userId, data.map { it.name })
                                )
                            } ?: RepositoriesResponse.Empty(userId, requestModel)
                    } ?: RepositoriesResponse.Error(userId, requestModel))
            }
        }
    }

    suspend fun syncUserRepos(
        userId: Long,
        userName: String,
        type: String,
        perPage: Int
    ): Flow<RepositoriesResponse> {
        var reposResponse: Response<List<RepositoryDto>>?
        return flow {
            try {
                reposResponse = remoteDataSource.getUsersRepos(userName, type, perPage)
            } catch (e: Throwable) {
                emit(RepositoriesResponse.SyncError(userId, null))
                return@flow
            }
            val requestModel = mapRepoHeader(reposResponse!!.headers())
            reposResponse
                .takeIf { it!!.isSuccessful && it.body() != null }
                ?.let { response ->
                    val data = response.body() as List<RepositoryDto>
                    synchronizeRepositories(userId, data)
                    emit(
                        if (data.isNotEmpty()) {
                            RepositoriesResponse.SyncSuccess(
                                userId,
                                requestModel,
                                RepositoriesModel(userId, data.map { it.name })
                            )
                        } else {
                            RepositoriesResponse.Empty(userId, requestModel)
                        }
                    )
                } ?: emit(RepositoriesResponse.SyncError(userId, requestModel))
        }
    }

    private suspend fun synchronizeWithDatabase(data: List<UserDto>): List<Long> {
        return localDataSource.saveData(mapper.dtosToEntities(data))
    }

    private suspend fun synchronizeRepositories(id: Long, data: List<RepositoryDto>) {
        return localDataSource.saveRepositories(id, data.map { RepositoryEntity(it.id, it.name) })
    }

    private fun mapHeader(headers: Headers): HeaderDataModel {
        return HeaderDataModel.DataModel(
            requestsLimit = headers["X-Ratelimit-Limit"]?.toInt() ?: -1,
            remainingRequest = headers["X-Ratelimit-Remaining"]?.toInt() ?: -1,
            timeUntilReset = headers["X-Ratelimit-Reset"]?.toLong()?.let { Date(it * 1000) }
                ?: Date(),
            nextSinceParam = headers["link"]
                ?.takeIf { it.contains("rel=\"next\"") }
                ?.let {
                    val sinceParam = it.substringBefore(">; rel=\"next\",")
                        .substringAfter("<https://api.github.com/users?since=").toLong()
                    return@let Next(sinceParam)
                } ?: SinceParam.AllLoaded
        )
    }

    private fun mapRepoHeader(headers: Headers): HeaderDataModel {
        return HeaderDataModel.DataModel(
            requestsLimit = headers["X-Ratelimit-Limit"]?.toInt() ?: -1,
            remainingRequest = headers["X-Ratelimit-Remaining"]?.toInt() ?: -1,
            timeUntilReset = headers["X-Ratelimit-Reset"]?.toLong()?.let { Date(it * 1000) }
                ?: Date(),
            nextSinceParam = Ignored
        )
    }
}