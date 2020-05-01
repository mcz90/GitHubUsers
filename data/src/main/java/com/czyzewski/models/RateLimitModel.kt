package com.czyzewski.models

import kotlinx.serialization.Serializable

@Serializable
data class RateLimitModel(
    val limit: Long,
    val remaining: Long,
    val reset: Long
)