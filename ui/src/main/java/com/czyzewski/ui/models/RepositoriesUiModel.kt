package com.czyzewski.ui.models

import androidx.annotation.StringRes

data class RepositoriesUiModel(
    val userId: Long? = null,
    val isSyncing: Boolean = false,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    @StringRes val issueResId: Int? = null,
    val repositories: List<String>? = null
)
