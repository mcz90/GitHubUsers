package com.czyzewski.ui.models

data class RateLimitUiModel(
    val remaining: Long,
    val total: Long,
    val timeToReset: String
)
