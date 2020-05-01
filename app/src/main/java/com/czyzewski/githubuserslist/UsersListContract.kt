package com.czyzewski.githubuserslist

import com.czyzewski.mvi.IMviRenderer
import com.czyzewski.mvi.IMviView
import com.czyzewski.mvi.ScreenIntent
import kotlinx.serialization.ImplicitReflectionSerializer

@ImplicitReflectionSerializer
interface IUsersListView : IMviView<UsersListState, UserListComponents>

@ImplicitReflectionSerializer
interface IUsersListRenderer : IMviRenderer<UsersListState, UserListComponents>

interface IUsersListNavigator {
    fun navigateUp()
    fun navigateToUserDetailsScreen(userId: Long, userName: String, transitionData: TransitionData)
}

sealed class UsersListIntent : ScreenIntent {
    object Init : UsersListIntent()
    object BackPress : UsersListIntent()
    object RefreshPress : UsersListIntent()
    data class SearchPress(val inSearchMode: Boolean) : UsersListIntent()
    data class SearchInputChanged(val query: String) : UsersListIntent()
    object ScrolledToBottom : UsersListIntent()
    data class UsersFetched(val list: List<Pair<String, Long>>) : UsersListIntent()
    data class SyncIconClicked(val data: Pair<String, Long>) : UsersListIntent()
    data class UserClicked(
        val userId: Long,
        val userName: String,
        val transitionData: TransitionData
    ) : UsersListIntent()
}

enum class LoadingState {
    INITIAL,
    SEARCH,
    MORE,
    LOADING_REPO,
    LOADING_RATE_LIMIT
}
