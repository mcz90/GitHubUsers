package com.czyzewski.net.dto

import kotlinx.serialization.Serializable

@Serializable
data class RateDto(
    val limit: Long,
    val remaining: Long,
    val reset: Long
)