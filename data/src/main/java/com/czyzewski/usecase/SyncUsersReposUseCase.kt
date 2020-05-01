package com.czyzewski.usecase

import com.czyzewski.models.RepositoriesModel
import com.czyzewski.models.RequestQueryModel
import com.czyzewski.usecase.SyncUsersReposUseCase.SyncReposData.Success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SyncUsersReposUseCase(
    private val getLocalReposUseCase: GetLocalReposUseCase,
    private val getRemoteReposUseCase: GetRemoteReposUseCase,
    private val saveReposUseCase: SaveReposUseCase,
    private val getRequestParamsUseCase: GetRequestParamsUseCase,
    private val saveRequestParamsUseCase: SaveRequestParamsUseCase
) {

    sealed class SyncReposData {
        var requestQueryModel: RequestQueryModel? = null

        data class Success(val repos: RepositoriesModel) : SyncReposData()
        data class Error(val userId: Long, val cause: Throwable) : SyncReposData()
        data class Loading(val userId: Long) : SyncReposData()
    }

    suspend fun syncRepos(userId: Long, userName: String): Flow<SyncReposData> {
        return flow {
            val localRepos = getLocalReposUseCase.getRepos(userId)

            if (localRepos.repositories.isEmpty()) {
                val responseModel = getRemoteReposUseCase.getRepos(
                    userId = userId,
                    userName = userName,
                    type = REPO_TYPE_QUERY_PARAM,
                    perPage = REPO_PER_PAGE_QUERY_PARAM
                ).also {
                    saveRequestParamsUseCase.saveRequestParams(it.requestQuery)
                    saveReposUseCase.saveRepos(it.body)
                }.body

                emit(
                    Success(responseModel)
                        .apply { requestQueryModel = getRequestParamsUseCase.getRequestParams() })
            } else {
                emit(
                    Success(localRepos)
                        .apply { requestQueryModel = getRequestParamsUseCase.getRequestParams() })
            }
        }
    }

    companion object {
        private const val REPO_TYPE_QUERY_PARAM = "owner"
        private const val REPO_PER_PAGE_QUERY_PARAM = 3
    }
}