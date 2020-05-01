package com.czyzewski.userdetails

import com.czyzewski.net.error.ErrorMapper
import com.czyzewski.net.error.ErrorSource.RATE_LIMIT
import com.czyzewski.net.error.ErrorSource.USER_DETAILS
import com.czyzewski.usecase.GetRateLimitUseCase.RateLimitData
import com.czyzewski.usecase.GetUserDetailsUseCase.UsersDetailsData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.serialization.ImplicitReflectionSerializer

@ImplicitReflectionSerializer
interface IUserDetailsReducer {
    fun <T> reduce(data: T): UserDetailsState
}

@ImplicitReflectionSerializer
@ExperimentalCoroutinesApi
class UserDetailsReducer(private val errorMapper: ErrorMapper) : IUserDetailsReducer {

    override fun <T> reduce(data: T): UserDetailsState {
        return when (data) {
            is UsersDetailsData -> mapUserDetails(data)
            is RateLimitData -> mapRateLimit(data)
            else -> throw IllegalStateException()
        }
    }

    private fun mapUserDetails(data: UsersDetailsData): UserDetailsState {
        return when (data) {
            is UsersDetailsData.Success -> UserDetailsState(userDetails = data.userDetails)
            is UsersDetailsData.Error ->{
                data.cause.printStackTrace()
                UserDetailsState(errorModel = errorMapper.map(USER_DETAILS, data.cause))
            }
            UsersDetailsData.Loading -> UserDetailsState(isLoading = true)
        }
    }

    private fun mapRateLimit(data: RateLimitData): UserDetailsState {
        return when (data) {
            is RateLimitData.Success -> UserDetailsState(rateLimit = data.rateLimit)
            is RateLimitData.Error -> UserDetailsState(errorModel = errorMapper.map(RATE_LIMIT, data.cause))
            RateLimitData.Loading -> UserDetailsState(isRateLimitLoading = true)
        }
    }
}
