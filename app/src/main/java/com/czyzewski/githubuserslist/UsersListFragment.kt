package com.czyzewski.githubuserslist

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import com.czyzewski.mocks.UsersListScreen
import com.czyzewski.mvi.MviFragment
import com.czyzewski.mvi.statereplayview.ScreenStateModel
import kotlinx.android.synthetic.main.fragment_users_list.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.serialization.ImplicitReflectionSerializer
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

@ImplicitReflectionSerializer
@ObsoleteCoroutinesApi
@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class UsersListFragment : MviFragment(R.layout.fragment_users_list) {

    override val view: IUsersListView by inject {
        parametersOf(viewModel)
    }

    override val viewModel: UsersListViewModel by viewModel {
        parametersOf(this@UsersListFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        components = UserListComponents(
            toolbarView = toolbarView,
            recyclerView = recyclerView,
            bottomProgressBar = bottomProgressBar,
            progressView = progressView,
            errorView = errorView
        )
        this.view.attach(components)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        view.onConfigurationChanged(newConfig.orientation)
    }

    override fun prepareMocks() = UsersListScreen.mocks
}
