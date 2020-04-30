package com.czyzewski.githubuserslist

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import com.czyzewski.BaseFragment
import kotlinx.android.synthetic.main.fragment_users_list.*
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

@InternalCoroutinesApi
class UsersListFragment : BaseFragment(R.layout.fragment_users_list) {

    private lateinit var view: IUsersListView

    private val viewModel: UsersListViewModel by viewModel {
        parametersOf(this@UsersListFragment)
    }

    private val renderer: UsersListRenderer by inject {
        parametersOf(this@UsersListFragment, viewModel)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.view = UsersListView(
            renderer = renderer,
            viewModel = viewModel,
            lifecycleOwner = viewLifecycleOwner
        ).apply {
            retain(
                recyclerView = recyclerView,
                toolbar = toolbarView,
                progressView = progressView,
                bottomProgressBar = bottomProgressBar,
                errorView = errorView
            )
            observe()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        renderer.onConfigurationChanged(newConfig.orientation)
    }
}