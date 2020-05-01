package com.czyzewski.githubuserslist

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.czyzewski.githubuserslist.UsersListIntent.*
import com.czyzewski.githubuserslist.mappers.IUserReducer
import com.czyzewski.mvi.MviViewModel
import com.czyzewski.usecase.*
import com.czyzewski.usecase.GetRateLimitUseCase.RateLimitData
import com.czyzewski.usecase.GetReposUseCase.ReposData
import com.czyzewski.usecase.GetStoredUsersUseCase.StoredUsersData
import com.czyzewski.usecase.GetUsersUseCase.UsersData
import com.czyzewski.usecase.SyncUsersReposUseCase.SyncReposData
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.ImplicitReflectionSerializer

@ObsoleteCoroutinesApi
@FlowPreview
@ImplicitReflectionSerializer
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class UsersListViewModel(
    private val navigator: IUsersListNavigator,
    private val getUsersUseCase: GetUsersUseCase,
    private val getStoredUsersUseCase: GetStoredUsersUseCase,
    private val getReposUseCase: GetReposUseCase,
    private val syncUsersReposUseCase: SyncUsersReposUseCase,
    private val getRateLimitUseCase: GetRateLimitUseCase,
    private val reducer: IUserReducer,
    viewLifecycleOwner: LifecycleOwner
) : MviViewModel<UsersListState, UsersListIntent>(viewLifecycleOwner) {

    override fun onIntentReceived(intent: UsersListIntent) {
        when (intent) {
            Init -> {
                getUsers(true)
                getRateLimit()
            }
            BackPress -> { }
            is SearchPress -> toggleSearchState(intent.inSearchMode)
            is SearchInputChanged -> getStoredUsers(intent.query)
            RefreshPress -> getRateLimit()
            ScrolledToBottom -> getUsers(false)
            is UsersFetched -> getRepos(intent.list)
            is SyncIconClicked -> syncUserRepos(intent.data.second, intent.data.first)
            is UserClicked -> navigator.navigateToUserDetailsScreen(
                intent.userId,
                intent.userName,
                intent.transitionData
            )
        }
    }

    private fun getUsers(first: Boolean) {
        viewModelScope.launch {
            getUsersUseCase.getUsers(first)
                .onStart { emit(UsersData.Loading(first)) }
                .catch { emit(UsersData.Error(it)) }
                .map { reducer.reduce(it) }
                .flowOn(Dispatchers.IO)
                .collect { state.value = it }
        }
    }

    private fun getStoredUsers(query: String) {
        viewModelScope.launch {
            getStoredUsersUseCase.get(query)
                .onStart { emit(StoredUsersData.Loading) }
                .catch { emit(StoredUsersData.Error(it)) }
                .map { reducer.reduce(it) }
                .flowOn(Dispatchers.IO)
                .collect { state.value = it }
        }
    }

    private fun getRepos(params: List<Pair<String, Long>>) {
        params.forEach { param ->
            viewModelScope.launch {
                getReposUseCase.getRepos(param.second, param.first)
                    .onStart { emit(ReposData.Loading(param.second)) }
                    .catch { emit(ReposData.Error(param.second, it)) }
                    .map { reducer.reduce(it) }
                    .flowOn(Dispatchers.IO)
                    .collect { state.value = it }
            }
        }
    }

    private fun syncUserRepos(userId: Long, userName: String) {
        viewModelScope.launch {
            syncUsersReposUseCase.syncRepos(userId, userName)
                .onStart { emit(SyncReposData.Loading(userId)) }
                .catch { emit(SyncReposData.Error(userId, it)) }
                .map { reducer.reduce(it) }
                .flowOn(Dispatchers.IO)
                .collect { state.value = it }
        }
    }

    private fun getRateLimit() {
        viewModelScope.launch {
            getRateLimitUseCase.getRateLimit()
                .onStart { emit(RateLimitData.Loading) }
                .catch { emit(RateLimitData.Error(it)) }
                .map { reducer.reduce(it) }
                .flowOn(Dispatchers.IO)
                .collect { state.value = it }
        }
    }

    private fun toggleSearchState(currentlyInSearchMode: Boolean) {
        viewModelScope.launch {
            state.value
                ?.takeIf { it.inSearchMode != currentlyInSearchMode }
                ?.let { state.value = state.value?.copy(inSearchMode = !currentlyInSearchMode) }
        }
    }


    fun getStaste() = state
}
