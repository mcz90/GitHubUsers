package com.czyzewski.userdetails

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.czyzewski.mvi.MviViewModel
import com.czyzewski.usecase.GetRateLimitUseCase
import com.czyzewski.usecase.GetRateLimitUseCase.RateLimitData
import com.czyzewski.usecase.GetUserDetailsUseCase
import com.czyzewski.usecase.GetUserDetailsUseCase.UsersDetailsData.Error
import com.czyzewski.usecase.GetUserDetailsUseCase.UsersDetailsData.Loading
import com.czyzewski.userdetails.UserDetailsIntent.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.serialization.ImplicitReflectionSerializer

@FlowPreview
@InternalCoroutinesApi
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@ImplicitReflectionSerializer
class UserDetailsViewModel(
    private val navigator: IUserDetailsNavigator,
    private val getUserDetailsUseCase: GetUserDetailsUseCase,
    private val getRateLimitUseCase: GetRateLimitUseCase,
    private val reducer: IUserDetailsReducer,
    viewLifecycleOwner: LifecycleOwner
) : MviViewModel<UserDetailsState, UserDetailsIntent>(viewLifecycleOwner) {

    override fun onIntentReceived(intent: UserDetailsIntent) = when (intent) {
        is Init -> {
            getUserDetails(intent.userId, intent.userName)
            getRateLimit()
        }
        BackPress -> navigator.navigateUp()
        RefreshPress -> getRateLimit()
    }

    private fun getUserDetails(userId: Long, userName: String) {
        viewModelScope.launch {
            getUserDetailsUseCase.getUserDetails(userId, userName)
                .onStart { emit(Loading) }
                .catch { emit(Error(it)) }
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
}
