package com.czyzewski.models

import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    val id: Long,
    val login: String,
    val nodeId: String,
    val avatarUrl: String,
    var repositories: Repositories
)

@Serializable
sealed class Repositories {
    @Serializable
    data class Loading(val isSyncing: Boolean = false) : Repositories()

    @Serializable
    data class Loaded(var data: RepositoriesModel, val isSyncing: Boolean = false) : Repositories()

    @Serializable
    object Empty : Repositories()

    @Serializable
    object Error : Repositories()
}