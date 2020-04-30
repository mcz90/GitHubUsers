package com.czyzewski.models

import androidx.annotation.StringRes

data class UserModel(
    val id: Long,
    val login: String,
    val nodeId: String,
    val avatarUrl: String,
    var repositories: Repositories
)

sealed class Repositories {
    object Loading : Repositories()
    data class Loaded(var data: RepositoriesModel) : Repositories()
    data class Empty(@StringRes val issueResId:Int) : Repositories()
    data class Error(@StringRes val issueResId:Int) : Repositories()
    object Syncing : Repositories()
    data class SyncSuccess(val data: RepositoriesModel) : Repositories()
    data class SyncError(@StringRes val issueResId: Int) : Repositories()
}