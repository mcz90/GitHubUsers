package com.czyzewski.net

import com.czyzewski.net.dto.RateLimitDto
import retrofit2.http.GET

interface RateLimitApiService {
    @GET("/rate_limit")
    suspend fun getRateLimit(): RateLimitDto
}