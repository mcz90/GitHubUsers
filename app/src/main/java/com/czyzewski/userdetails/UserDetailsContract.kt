package com.czyzewski.userdetails

import com.czyzewski.mvi.IMviRenderer
import com.czyzewski.mvi.IMviView
import com.czyzewski.mvi.ScreenIntent
import kotlinx.serialization.ImplicitReflectionSerializer

@ImplicitReflectionSerializer
interface IUserDetailsView : IMviView<UserDetailsState, UserDetailsComponents>

@ImplicitReflectionSerializer
interface IUserDetailsRenderer : IMviRenderer<UserDetailsState, UserDetailsComponents>

interface IUserDetailsNavigator {
    fun navigateUp()
}

sealed class UserDetailsIntent : ScreenIntent {
    data class Init(val userId: Long, val userName: String) : UserDetailsIntent()
    object BackPress : UserDetailsIntent()
    object RefreshPress : UserDetailsIntent()
}
