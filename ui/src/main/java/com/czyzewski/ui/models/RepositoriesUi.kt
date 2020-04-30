package com.czyzewski.ui.models

import androidx.annotation.StringRes

sealed class RepositoriesUi {
    data class Empty(@StringRes val issueResId: Int) : RepositoriesUi()
    data class Error(@StringRes val issueResId: Int) : RepositoriesUi()
    object Loading : RepositoriesUi()
    object Syncing : RepositoriesUi()
    data class Loaded(val data: RepositoriesUiModel) : RepositoriesUi()
    data class SyncError(@StringRes val issueResId: Int) : RepositoriesUi()
    data class SyncSuccess(val data: RepositoriesUiModel) : RepositoriesUi()
}