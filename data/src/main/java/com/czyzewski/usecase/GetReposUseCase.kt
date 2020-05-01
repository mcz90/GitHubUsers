package com.czyzewski.usecase

import com.czyzewski.models.RepositoriesModel
import com.czyzewski.models.RequestQueryModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetReposUseCase(
    private val getLocalReposUseCase: GetLocalReposUseCase,
    private val getRemoteReposUseCase: GetRemoteReposUseCase,
    private val saveReposUseCase: SaveReposUseCase,
    private val shouldSyncUserUseCase: ShouldSyncUserUseCase,
    private val getRequestParamsUseCase: GetRequestParamsUseCase,
    private val saveRequestParamsUseCase: SaveRequestParamsUseCase
) {

    sealed class ReposData {
        var requestQueryModel: RequestQueryModel? = null

        data class Success(val repos: RepositoriesModel) : ReposData()
        data class Error(val userId: Long, val cause: Throwable) : ReposData()
        data class Loading(val userId: Long) : ReposData()
    }

    suspend fun getRepos(userId: Long, userName: String): Flow<ReposData> {
        return flow {
            val localRepos = getLocalReposUseCase.getRepos(userId)
            val shouldSync = shouldSyncUserUseCase.get(userId)
            if (!shouldSync) {
                emit(
                    ReposData.Success(localRepos)
                        .apply { requestQueryModel = getRequestParamsUseCase.getRequestParams() })
            } else if (localRepos.repositories.isEmpty()) {
                val responseModel = getRemoteReposUseCase.getRepos(
                    userId = userId,
                    userName = userName,
                    type = REPO_TYPE_QUERY_PARAM,
                    perPage = REPO_PER_PAGE_QUERY_PARAM
                ).also {
                    saveReposUseCase.saveRepos(it.body)
                    saveRequestParamsUseCase.saveRequestParams(it.requestQuery)
                }.body
                emit(
                    ReposData.Success(responseModel)
                        .apply {
                            requestQueryModel = getRequestParamsUseCase.getRequestParams()
                        })
            } else {
                emit(
                    ReposData.Success(localRepos)
                        .apply {
                            requestQueryModel = getRequestParamsUseCase.getRequestParams()
                        })
            }
        }
    }

    companion object {
        private const val REPO_TYPE_QUERY_PARAM = "owner"
        private const val REPO_PER_PAGE_QUERY_PARAM = 3
    }
}