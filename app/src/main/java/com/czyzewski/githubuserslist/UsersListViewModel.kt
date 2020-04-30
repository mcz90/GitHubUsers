package com.czyzewski.githubuserslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.czyzewski.githubuserslist.UsersListIntent.*
import com.czyzewski.githubuserslist.usevase.GetUsersUserCase
import com.czyzewski.models.RepositoriesResponse
import com.czyzewski.models.UsersResponse.*
import com.czyzewski.repository.UsersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class UsersListViewModel(
    private val navigator: IUsersListNavigator,
    private val repository: UsersRepository,
    private val getUsersUserCase: GetUsersUserCase
) : IUsersListViewModel, ViewModel() {

    private val _state = MutableLiveData<UsersListState>()
    override val state: LiveData<UsersListState>
        get() = _state

    override val coroutineContext: CoroutineContext = Dispatchers.IO

    init {
        getUsers(true)
    }

    override fun onIntentReceived(intent: UsersListIntent) = when (intent) {
        BackPress -> {
        }
        ScrolledToBottom -> getUsers(false)
        is UsersFetched -> getRepositories(intent.list)
        is SyncIconClicked -> syncUserRepositories(intent.data)
    }

    private fun getUsers(first: Boolean) {
        viewModelScope.launch {
            getUsersUserCase.getUsers(first)
                .map {
                    when (it) {
                        is Loading -> UsersListState.UsersLoading
                        is Loaded -> UsersListState.UsersLoaded(it.data, it.headerDataModel)
                        is MoreLoading -> UsersListState.MoreUsersLoading
                        AllLoaded -> UsersListState.AllLoaded
                        is Empty -> UsersListState.UsersEmpty(
                            it.headerDataModel,
                            R.string.users_list_users_error
                        )
                        is Error -> UsersListState.UsersError(
                            it.headerDataModel,
                            R.string.users_list_users_error
                        )
                    }
                }
                .flowOn(Dispatchers.IO)
                .collect { _state.value = it }
        }
    }

    private fun syncUserRepositories(pair: Pair<String, Long>) {
        viewModelScope.launch {
            repository.syncUserRepos(
                userId = pair.second,
                userName = pair.first,
                type = "owner",
                perPage = 3
            ).map {
                when (it) {
                    is RepositoriesResponse.Syncing -> UsersListState.RepositoriesSyncing(
                        it.userId
                    )
                    is RepositoriesResponse.SyncSuccess -> UsersListState.SyncSuccess(
                        it.userId,
                        it.repositories,
                        it.headerDataModel
                    )
                    is RepositoriesResponse.Empty -> UsersListState.RepositoriesEmpty(
                        it.userId,
                        it.headerDataModel,
                        R.string.users_list_users_repositories_empty
                    )
                    is RepositoriesResponse.SyncError -> UsersListState.SyncError(
                        it.headerDataModel,
                        it.userId,
                        R.string.users_list_users_repositories_error
                    )
                    else -> throw IllegalStateException()
                }
            }
                .onStart { RepositoriesResponse.Syncing(pair.second) }
                .flowOn(Dispatchers.IO)
                .collect { _state.value = it }
        }
    }

    private fun getRepositories(list: List<Pair<String, Long>>) {
        list.forEach { pair ->
            viewModelScope.launch {
                repository.getUserRepos(
                    userId = pair.second,
                    userName = pair.first,
                    type = "owner",
                    perPage = 3
                ).map {
                    when (it) {
                        is RepositoriesResponse.Loading -> UsersListState.RepositoriesLoading(
                            it.userId
                        )
                        is RepositoriesResponse.Loaded -> UsersListState.RepositoriesLoaded(
                            it.userId,
                            it.repositories,
                            it.headerDataModel
                        )
                        is RepositoriesResponse.Empty -> UsersListState.RepositoriesEmpty(
                            it.userId,
                            it.headerDataModel,
                            R.string.users_list_users_repositories_empty
                        )
                        is RepositoriesResponse.Error -> UsersListState.RepositoriesError(
                            it.userId,
                            it.headerDataModel,
                            R.string.users_list_users_repositories_error
                        )
                        else -> throw IllegalStateException()
                    }
                }
                    .onStart { RepositoriesResponse.Loading(pair.second) }
                    .flowOn(Dispatchers.IO)
                    .collect { _state.value = it }
            }
        }
    }
}