package com.czyzewski.githubuserslist.mappers

import com.czyzewski.githubuserslist.LoadingState.*
import com.czyzewski.githubuserslist.UsersListState
import com.czyzewski.net.error.ErrorMapper
import com.czyzewski.net.error.ErrorSource
import com.czyzewski.net.error.ErrorSource.REPOS
import com.czyzewski.net.error.ErrorSource.USERS
import com.czyzewski.usecase.GetRateLimitUseCase.RateLimitData
import com.czyzewski.usecase.GetReposUseCase.ReposData
import com.czyzewski.usecase.GetStoredUsersUseCase.StoredUsersData
import com.czyzewski.usecase.GetUsersUseCase.UsersData
import com.czyzewski.usecase.SyncUsersReposUseCase.SyncReposData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.serialization.ImplicitReflectionSerializer

@ImplicitReflectionSerializer
interface IUserReducer {
    fun <T> reduce(data: T): UsersListState
}

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@ImplicitReflectionSerializer
class UserReducer(private val errorMapper: ErrorMapper) : IUserReducer {

    override fun <T> reduce(data: T): UsersListState {
        return when (data) {
            is UsersData -> mapUsers(data)
            is StoredUsersData -> mapStoredUsers(data)
            is ReposData -> mapRepos(data)
            is SyncReposData -> mapSyncRepos(data)
            is RateLimitData -> mapRateLimit(data)
            else -> throw IllegalStateException()
        }
    }

    private fun mapUsers(data: UsersData): UsersListState {
        return when (data) {
            is UsersData.Success -> UsersListState(
                users = data.users
            )
            is UsersData.Error -> UsersListState(
                errorModel = errorMapper.map(USERS, data.cause)
            )
            is UsersData.Loading ->
                if (data.first) UsersListState(loadingState = INITIAL)
                else UsersListState(loadingState = MORE)
        }
    }

    private fun mapStoredUsers(data: StoredUsersData): UsersListState {
        return when (data) {
            is StoredUsersData.Success -> UsersListState(
                users = data.users
            )
            is StoredUsersData.Error -> UsersListState(
                errorModel = errorMapper.map(USERS, data.cause)
            )
            is StoredUsersData.Loading -> UsersListState(
                loadingState = SEARCH
            )
        }
    }

    private fun mapRepos(data: ReposData): UsersListState {
        return when (data) {
            is ReposData.Success -> UsersListState(
                repositories = data.repos
            )
            is ReposData.Error -> UsersListState(
                updatedUserId = data.userId,
                errorModel = errorMapper.map(REPOS, data.cause)
            )
            is ReposData.Loading -> UsersListState(
                updatedUserId = data.userId,
                loadingState = LOADING_REPO
            )
        }
    }

    private fun mapSyncRepos(data: SyncReposData): UsersListState {
        return when (data) {
            is SyncReposData.Success -> UsersListState(
                isSyncingRepos = true,
                repositories = data.repos
            )
            is SyncReposData.Error -> UsersListState(
                isSyncingRepos = true,
                updatedUserId = data.userId,
                errorModel = errorMapper.map(REPOS, data.cause)
            )
            is SyncReposData.Loading -> UsersListState(
                isSyncingRepos = true,
                updatedUserId = data.userId,
                loadingState = LOADING_REPO
            )
        }
    }

    private fun mapRateLimit(data: RateLimitData): UsersListState {
        return when (data) {
            is RateLimitData.Success -> UsersListState(
                rateLimit = data.rateLimit
            )
            is RateLimitData.Error -> UsersListState(
                errorModel = errorMapper.map(ErrorSource.RATE_LIMIT, data.cause)
            )
            RateLimitData.Loading -> UsersListState(
                loadingState = LOADING_RATE_LIMIT
            )
        }
    }
}
