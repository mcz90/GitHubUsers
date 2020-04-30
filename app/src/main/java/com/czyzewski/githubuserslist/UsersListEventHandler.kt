package com.czyzewski.githubuserslist

import com.czyzewski.githubuserslist.UsersListIntent.*
import kotlinx.coroutines.InternalCoroutinesApi

interface IUsersListEventHandler {
    fun handle(event: UsersListIntent)
}

@InternalCoroutinesApi
class UsersListEventHandler(private val viewModel: IUsersListViewModel) :
    IUsersListEventHandler {

    override fun handle(event: UsersListIntent) = when (event) {
        BackPress -> viewModel.onIntentReceived(BackPress)
        ScrolledToBottom -> viewModel.onIntentReceived(ScrolledToBottom)
        is UsersFetched -> viewModel.onIntentReceived(UsersFetched(event.list))
        is SyncIconClicked -> viewModel.onIntentReceived(SyncIconClicked(event.data))
    }
}