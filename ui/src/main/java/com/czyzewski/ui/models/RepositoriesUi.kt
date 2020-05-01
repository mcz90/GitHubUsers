package com.czyzewski.ui.models

import androidx.annotation.StringRes

sealed class RepositoriesUi {
    data class Empty(@StringRes val issueResId: Int) : RepositoriesUi()
    data class Error(@StringRes val issueResId: Int) : RepositoriesUi()
    data class Loading(val isSyncing: Boolean) : RepositoriesUi()
    data class Loaded(val data: RepositoriesUiModel, val isSyncing: Boolean) : RepositoriesUi()
}
