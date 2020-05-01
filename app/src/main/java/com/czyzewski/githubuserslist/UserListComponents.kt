package com.czyzewski.githubuserslist

import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.czyzewski.mvi.ViewComponents
import com.czyzewski.ui.view.ErrorView
import com.czyzewski.ui.view.ProgressView
import com.czyzewski.ui.view.ToolbarView

data class UserListComponents(
    val toolbarView: ToolbarView,
    val recyclerView: RecyclerView,
    val bottomProgressBar: ProgressBar,
    val progressView: ProgressView,
    val errorView: ErrorView
) : ViewComponents
