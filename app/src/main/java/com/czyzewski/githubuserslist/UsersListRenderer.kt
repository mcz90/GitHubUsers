package com.czyzewski.githubuserslist

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.czyzewski.githubuserslist.LoadingState.*
import com.czyzewski.githubuserslist.UsersListIntent.*
import com.czyzewski.net.error.ErrorModel.*
import com.czyzewski.net.error.ErrorSource.*
import com.czyzewski.toUiModel
import com.czyzewski.ui.addScrollToBottomListener
import com.czyzewski.ui.view.ErrorView
import com.czyzewski.ui.view.ProgressView
import com.czyzewski.ui.view.ToolbarView
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.serialization.ImplicitReflectionSerializer

@ImplicitReflectionSerializer
@InternalCoroutinesApi
class UsersListRenderer(
    private val eventHandler: IUsersListEventHandler,
    private val adapter: UsersListAdapter
) : IUsersListRenderer {

    private lateinit var recyclerView: RecyclerView
    private lateinit var toolbar: ToolbarView
    private lateinit var progressView: ProgressView
    private lateinit var bottomProgressBar: ProgressBar
    private lateinit var errorView: ErrorView

    init {
        eventHandler.handle(Init)
    }


    override fun render(state: UsersListState) {
        state.rateLimit?.let {
            toolbar.hideProgress()
            toolbar.setRateLimit(it.toUiModel())
        }
        state.inSearchMode?.let {
            when (it) {
                true -> toolbar.enterSearchMode()
                false -> toolbar.exitSearchMode()
            }
        }

        state.users?.let { usersList ->
            adapter.setList(usersList)
            adapter.setOnUserClickListener {
                eventHandler.handle(UserClicked(it.first, it.second, it.third))
            }
            eventHandler.handle(UsersFetched(usersList.map { user -> user.login to user.id }))
        }

        state.repositories?.let { reposModel ->
            adapter.repositoriesLoaded(reposModel, state.isSyncingRepos)
            adapter.setOnSyncIconClickListener { eventHandler.handle(SyncIconClicked(it)) }
        }

        recyclerView.isVisible =
            (state.errorModel == null || state.loadingState == null) && state.loadingState != INITIAL

        state.loadingState?.let { loadingState ->
            if (loadingState == LOADING_REPO) {
                adapter.repositoriesLoadingStarted(
                    state.updatedUserId ?: throw IllegalStateException("sdfsdfsdf"),
                    state.isSyncingRepos
                )
            }
        }
        progressView.isVisible = state.loadingState?.let { it == INITIAL } ?: false
        bottomProgressBar.isVisible = state.loadingState?.let { it == MORE } ?: false

        state.loadingState
            ?.takeIf { it == LOADING_RATE_LIMIT }
            ?.let { toolbar.showProgress() }

        state.errorModel?.let { error ->
            when (error.source) {
                USERS -> {
                    errorView.setErrorText(
                        when (error) {
                            is ApiError -> error.errorMessage
                            is DatabaseError -> error.errorMessage
                            is UnhandledError -> "UnhandledError"
                            is NoInternetError -> "NoInternetError"
                            is ConnectionLostError -> error.errorMessage
                        }
                    )
                    errorView.isVisible = true
                }
                REPOS -> adapter.repositoriesLoadingError(
                    state.updatedUserId ?: throw IllegalStateException("sdfsdfsdf")
                )
                RATE_LIMIT -> {
                    toolbar.hideProgress()
                    toolbar.showError()
                }
                else -> throw IllegalStateException("sdfsdfsdf")
            }
        }
        errorView.isVisible = state.errorModel?.let { it.source == USERS } ?: false
    }

    override fun attach(components: UserListComponents) {
        this.recyclerView = components.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@UsersListRenderer.adapter
            addScrollToBottomListener { eventHandler.handle(ScrolledToBottom) }
        }
        this.toolbar = components.toolbarView.apply {
            onBackClick { eventHandler.handle(BackPress) }
            onRefreshClick { eventHandler.handle(RefreshPress) }
            onSearchClick { eventHandler.handle(SearchPress(it)) }
            onSearchInputChanged {
                // TODO eventHandler.handle(SearchInputChanged(it))
            }
        }
        this.progressView = components.progressView
        this.bottomProgressBar = components.bottomProgressBar
        this.errorView = components.errorView
    }

    override fun onConfigurationChanged(orientation: Int) {
        this.recyclerView.apply {
            layoutManager = GridLayoutManager(
                context,
                when (orientation) {
                    ORIENTATION_PORTRAIT -> 1
                    ORIENTATION_LANDSCAPE -> 2
                    else -> 1
                }
            )
        }
        adapter.notifyItemRangeChanged(0, adapter.itemCount)
    }
}
