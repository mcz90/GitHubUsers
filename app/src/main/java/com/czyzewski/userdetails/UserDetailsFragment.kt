package com.czyzewski.userdetails

import android.os.Bundle
import android.view.View
import androidx.transition.TransitionInflater.from
import com.czyzewski.githubuserslist.R
import com.czyzewski.mocks.UserDetailsScreen
import com.czyzewski.mvi.MviFragment
import com.czyzewski.mvi.statereplayview.ScreenStateModel
import kotlinx.android.synthetic.main.fragment_user_details.*
import kotlinx.android.synthetic.main.fragment_user_details.errorView
import kotlinx.android.synthetic.main.fragment_user_details.progressView
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
class UserDetailsFragment : MviFragment(R.layout.fragment_user_details) {

    override val view: IUserDetailsView by inject {
        parametersOf(this@UserDetailsFragment, viewModel)
    }

    override val viewModel: UserDetailsViewModel by viewModel {
        parametersOf(this@UserDetailsFragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = from(context).inflateTransition(android.R.transition.move)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        components = UserDetailsComponents(
            toolbar = toolbarView,
            progressView = progressView,
            errorView = errorView,
            userImage = userImage,
            reposHeaderView = reposHeaderView,
            gistsHeaderView = gistsHeaderView,
            followersHeaderView = followersHeaderView,
            nameLabelWithIcon = userNameLabelWithIcon,
            locationLabelWithIcon = userLocationLabelWithIcon,
            companyLabelWithIcon = userCompanyLabelWithIcon,
            twitterLabelWithIcon = userTwitterLabelWithIcon
        )
        this.view.attach(components)
    }

    override fun prepareMocks(): List<ScreenStateModel> = UserDetailsScreen.mocks
}
