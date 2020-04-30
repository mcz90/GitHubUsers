package com.czyzewski.githubuserslist

import android.widget.ProgressBar
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.czyzewski.ui.ErrorView
import com.czyzewski.ui.ProgressView
import com.czyzewski.ui.ToolbarView
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class UsersListView(
    private val renderer: IUsersListRenderer,
    private val viewModel: UsersListViewModel,
    private val lifecycleOwner: LifecycleOwner
) : IUsersListView {

    override fun render(state: UsersListState) {
        renderer.render(state)
    }

    override fun observe() {
        viewModel.state.observe(lifecycleOwner, Observer { renderer.render(it) } )
    }

    override fun retain(
        recyclerView: RecyclerView,
        toolbar: ToolbarView,
        progressView: ProgressView,
        bottomProgressBar: ProgressBar,
        errorView: ErrorView
    ) {
        renderer.retain(recyclerView, toolbar, progressView, bottomProgressBar, errorView)
    }
}