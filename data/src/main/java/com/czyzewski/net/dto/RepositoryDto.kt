package com.czyzewski.net.dto

import kotlinx.serialization.Serializable

@Serializable
data class RepositoryDto(
    val id: Long,
    val name: String
)
