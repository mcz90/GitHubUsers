package com.czyzewski.ui.models

data class UserUiModel(
    val userId: Long,
    val userName: String,
    val avatarUrl: String,
    val repositories: RepositoriesUi
)
