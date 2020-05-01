package com.czyzewski.net

import com.czyzewski.net.dto.RateLimitDto

@Suppress("UNCHECKED_CAST")
class RemoteRateLimitSource(private val rateLimitApiService: RateLimitApiService) {
    suspend fun getRateLimit(): Response<RateLimitDto> {
        return try {
            Response.Success(rateLimitApiService.getRateLimit())
        } catch (exception: Throwable) {
            Response.Error(exception)
        } as Response<RateLimitDto>
    }
}

sealed class Response<ResponseData> {
    data class Success<ResponseData>(val data: ResponseData) : Response<ResponseData>()
    data class Error(val cause: Throwable) : Response<Nothing>()
}