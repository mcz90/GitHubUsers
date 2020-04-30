package com.czyzewski.githubuserslist

import android.content.Context
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.czyzewski.githubuserslist.UsersListIntent.*
import com.czyzewski.githubuserslist.UsersListState.*
import com.czyzewski.models.HeaderDataModel
import com.czyzewski.ui.ErrorView
import com.czyzewski.ui.ProgressView
import com.czyzewski.ui.ToolbarView
import kotlinx.coroutines.InternalCoroutinesApi
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


interface IUsersListRenderer {
    fun render(state: UsersListState)
    fun retain(
        recyclerView: RecyclerView,
        toolbar: ToolbarView,
        progressView: ProgressView,
        bottomProgressBar: ProgressBar,
        errorView: ErrorView
    )

    fun onConfigurationChanged(orientation: Int)

}

@InternalCoroutinesApi
class UsersListRenderer(
    context: Context,
    private val eventHandler: IUsersListEventHandler
) : IUsersListRenderer {

    private val adapter = UsersListAdapter(context)

    private lateinit var recyclerView: RecyclerView
    private lateinit var toolbar: ToolbarView
    private lateinit var progressView: ProgressView
    private lateinit var bottomProgressBar: ProgressBar
    private lateinit var errorView: ErrorView


    override fun render(state: UsersListState) = when (state) {
        is UsersEmpty -> {
            recyclerView.isVisible = false
            bottomProgressBar.isVisible = false
            progressView.hide()
            toolbar.setRequestData(state.headerDataModel)
            errorView.hide()
        }
        AllLoaded -> {
            recyclerView.isVisible = true
            bottomProgressBar.isVisible = false
            progressView.hide()
            errorView.hide()
        }
        UsersLoading -> {
            recyclerView.isVisible = false
            bottomProgressBar.isVisible = false
            progressView.show()
            errorView.hide()
        }
        MoreUsersLoading -> {
            recyclerView.isVisible = true
            bottomProgressBar.isVisible = true
            progressView.hide()
            errorView.hide()
        }
        is UsersError -> {
            recyclerView.isVisible = false
            bottomProgressBar.isVisible = false
            progressView.hide()
            errorView.show()
            toolbar.setRequestData(state.headerDataModel)
            errorView.setErrorText("Error")
        }
        is UsersLoaded -> {
            recyclerView.isVisible = true
            bottomProgressBar.isVisible = false
            progressView.hide()
            errorView.hide()
            adapter.setList(state.data)
            adapter.setOnSyncIconClickListener {
                eventHandler.handle(SyncIconClicked(it))
            }
            toolbar.setRequestData(state.headerDataModel)
            eventHandler.handle(UsersFetched(state.data.map { it.login to it.id }))
        }
        is RepositoriesEmpty -> {
            toolbar.setRequestData(state.headerDataModel)
            adapter.repositoriesEmpty(state.userId, state.issueResId)
        }
        is RepositoriesLoading -> {
            adapter.repositoriesLoadingStarted(state.userId)
        }
        is RepositoriesSyncing -> {
            adapter.repositoriesSyncingStarted(state.userId)
        }
        is RepositoriesError -> {
            toolbar.setRequestData(state.headerDataModel)
            adapter.repositoriesLoadingError(state.userId, state.issueResId)
        }
        is RepositoriesLoaded -> {
            toolbar.setRequestData(state.headerDataModel)
            adapter.repositoriesLoaded(state.userId, state.data)
        }
        is SyncSuccess -> {
            toolbar.setRequestData(state.headerDataModel)
            adapter.repositoriesSyncSuccess(state.userId, state.data)
        }
        is SyncError -> {
            state.headerDataModel?.let {
                toolbar.setRequestData(it)
            }
            adapter.repositoriesSyncError(state.userId, state.issueResId)
        }
    }

    override fun retain(
        recyclerView: RecyclerView,
        toolbar: ToolbarView,
        progressView: ProgressView,
        bottomProgressBar: ProgressBar,
        errorView: ErrorView
    ) {
        this.recyclerView = recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@UsersListRenderer.adapter
            addScrollToBottomListener { eventHandler.handle(ScrolledToBottom) }
        }
        this.toolbar = toolbar.apply { onBackClick { eventHandler.handle(BackPress) } }
        this.progressView = progressView
        this.bottomProgressBar = bottomProgressBar
        this.errorView = errorView
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

    private fun RecyclerView.addScrollToBottomListener(onScrolledToBottom: () -> Unit) {
        addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (isScrolledToBottom(dy)) {
                    onScrolledToBottom()
                }
            }

            private fun isScrolledToBottom(dy: Int) =
                adapter?.let {
                    require(layoutManager is LinearLayoutManager)
                    it.itemCount > 0 && dy > 0 && (layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition() == it.itemCount - 1
                } ?: false
        })
    }

    private fun ToolbarView.setRequestData(headerDataModel: HeaderDataModel) {
        if (headerDataModel is HeaderDataModel.DataModel) {
            headerDataModel.apply {
                val formatter: DateFormat =
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                formatter.timeZone = TimeZone.getTimeZone("GMT+2")
                val date = formatter.format(timeUntilReset)
                toolbar.setRateLimit(
                    timeToReset = date,
                    remaining = remainingRequest,
                    total = requestsLimit
                )
            }
        }
    }
}