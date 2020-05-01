package com.czyzewski.userdetails

import com.czyzewski.userdetails.UserDetailsIntent.BackPress
import com.czyzewski.userdetails.UserDetailsIntent.Init
import com.czyzewski.userdetails.UserDetailsIntent.RefreshPress
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.serialization.ImplicitReflectionSerializer

interface IUserDetailsEventHandler {
    fun handle(event: UserDetailsIntent)
}

@FlowPreview
@InternalCoroutinesApi
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@ImplicitReflectionSerializer
class UserDetailsEventHandler(private val viewModel: UserDetailsViewModel) : IUserDetailsEventHandler {

    override fun handle(event: UserDetailsIntent) {
        when (event) {
            is Init -> viewModel.onIntentReceived(Init(event.userId, event.userName))
            BackPress -> viewModel.onIntentReceived(BackPress)
            RefreshPress -> viewModel.onIntentReceived(RefreshPress)
        }
    }
}
