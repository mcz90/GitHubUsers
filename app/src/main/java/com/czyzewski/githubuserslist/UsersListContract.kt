package com.czyzewski.githubuserslist

import android.widget.ProgressBar
import androidx.annotation.StringRes
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.czyzewski.models.RepositoriesModel
import com.czyzewski.models.HeaderDataModel
import com.czyzewski.models.UserModel
import com.czyzewski.ui.ErrorView
import com.czyzewski.ui.ProgressView
import com.czyzewski.ui.ToolbarView
import kotlinx.coroutines.CoroutineScope

interface IUsersListView : LifecycleObserver {
    fun render(state: UsersListState)
    fun observe()
    fun retain(
        recyclerView: RecyclerView,
        toolbar: ToolbarView,
        progressView: ProgressView,
        bottomProgressBar: ProgressBar,
        errorView: ErrorView
    )
}

interface IUsersListViewModel : CoroutineScope {
    val state: LiveData<UsersListState>
    fun onIntentReceived(intent: UsersListIntent)
}

interface IUsersListNavigator {
    fun navigateUp()
}

sealed class UsersListIntent {
    object BackPress : UsersListIntent()
    object ScrolledToBottom : UsersListIntent()
    data class UsersFetched(val list: List<Pair<String, Long>>) : UsersListIntent()
    data class SyncIconClicked(val data : Pair<String, Long>) : UsersListIntent()
}

sealed class UsersListState {
    object UsersLoading : UsersListState()
    data class UsersEmpty(val headerDataModel: HeaderDataModel, @StringRes val issueResId: Int) : UsersListState()
    data class UsersLoaded(val data: List<UserModel>, val headerDataModel: HeaderDataModel) : UsersListState()
    object MoreUsersLoading : UsersListState()
    object AllLoaded : UsersListState()
    data class UsersError(val headerDataModel: HeaderDataModel, @StringRes val issueResId: Int) : UsersListState()

    data class RepositoriesLoading(val userId: Long) : UsersListState()
    data class RepositoriesLoaded(val userId: Long, val data: RepositoriesModel, val headerDataModel: HeaderDataModel) : UsersListState()
    data class RepositoriesEmpty(val userId: Long, val headerDataModel: HeaderDataModel, @StringRes val issueResId: Int) : UsersListState()
    data class RepositoriesError(val userId: Long, val headerDataModel: HeaderDataModel, @StringRes val issueResId: Int) : UsersListState()
    data class RepositoriesSyncing(val userId: Long) : UsersListState()
    data class SyncSuccess(val userId: Long, val data: RepositoriesModel, val headerDataModel: HeaderDataModel) : UsersListState()
    data class SyncError(val headerDataModel: HeaderDataModel?, val userId: Long, @StringRes val issueResId: Int) : UsersListState()
}