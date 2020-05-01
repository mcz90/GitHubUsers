package com.czyzewski.githubuserslist

import com.czyzewski.githubuserslist.UsersListIntent.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.serialization.ImplicitReflectionSerializer

interface IUsersListEventHandler {
    fun handle(event: UsersListIntent)
}

@FlowPreview
@InternalCoroutinesApi
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@ImplicitReflectionSerializer
class UsersListEventHandler(private val viewModel: UsersListViewModel) : IUsersListEventHandler {

    override fun handle(event: UsersListIntent) {
        when (event) {
            Init -> viewModel.onIntentReceived(Init)
            BackPress -> viewModel.onIntentReceived(BackPress)
            RefreshPress -> viewModel.onIntentReceived(RefreshPress)
            is SearchPress -> viewModel.onIntentReceived(SearchPress(event.inSearchMode))
            is SearchInputChanged -> viewModel.onIntentReceived(SearchInputChanged(event.query))
            ScrolledToBottom -> viewModel.onIntentReceived(ScrolledToBottom)
            is UsersFetched -> viewModel.onIntentReceived(UsersFetched(event.list))
            is SyncIconClicked -> viewModel.onIntentReceived(SyncIconClicked(event.data))
            is UserClicked -> viewModel.onIntentReceived(UserClicked(event.userId, event.userName, event.transitionData))
        }
    }
}
