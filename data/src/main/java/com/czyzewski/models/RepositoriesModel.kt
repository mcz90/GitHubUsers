package com.czyzewski.models

import kotlinx.serialization.Serializable

@Serializable
data class RepositoriesModel(
    val userId: Long,
    val repositories: List<RepositoryModel>
)

@Serializable
data class RepositoryModel(
    val id: Long,
    val name: String
)