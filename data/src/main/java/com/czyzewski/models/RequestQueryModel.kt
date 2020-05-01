package com.czyzewski.models

import kotlinx.serialization.Serializable

@Serializable
data class RequestQueryModel(
    val sinceParam: Long? = null,
    val allLoaded: Boolean? = null
)
