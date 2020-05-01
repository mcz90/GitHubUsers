package com.czyzewski.usecase

import com.czyzewski.database.LocalRateLimitSource
import com.czyzewski.models.RateLimitModel
import com.czyzewski.net.RemoteRateLimitSource
import com.czyzewski.net.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetRateLimitUseCase(
    private val remoteRateLimitSource: RemoteRateLimitSource,
    private val localRateLimitSource: LocalRateLimitSource
) {

    sealed class RateLimitData {
        data class Success(val rateLimit: RateLimitModel) : RateLimitData()
        data class Error(val cause: Throwable) : RateLimitData()
        object Loading : RateLimitData()
    }

    suspend fun getRateLimit(): Flow<RateLimitData> {
        return flow {
            val localRateLimit = localRateLimitSource.getRateLimit()
            val remoteRateLimit = remoteRateLimitSource.getRateLimit()
            emit(
                RateLimitData.Success(
                    if (remoteRateLimit is Response.Success) {
                        localRateLimitSource.saveRateLimit(remoteRateLimit.data.toModel())
                        (remoteRateLimit).data.toModel()
                    } else {
                        localRateLimit.toModel()
                    }
                )
            )
        }
    }
}