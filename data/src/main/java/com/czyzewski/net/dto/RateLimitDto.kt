package com.czyzewski.net.dto

import com.czyzewski.models.RateLimitModel
import kotlinx.serialization.Serializable

@Serializable
data class RateLimitDto(val rate: RateDto) {

    fun toModel() =
        RateLimitModel(
            limit = rate.limit,
            remaining = rate.remaining,
            reset = rate.reset
        )
}