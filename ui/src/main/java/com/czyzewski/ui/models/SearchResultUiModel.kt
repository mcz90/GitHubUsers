package com.czyzewski.ui.models


data class SearchResultUiModel(
    val userId: Long,
    val userName: String,
    val avatarUrl: String,
    val isStored: Boolean
)