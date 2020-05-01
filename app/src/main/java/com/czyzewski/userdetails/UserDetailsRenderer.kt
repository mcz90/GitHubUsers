package com.czyzewski.userdetails

import android.widget.ImageView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.czyzewski.githubuserslist.R
import com.czyzewski.net.error.ErrorModel.*
import com.czyzewski.net.error.ErrorSource.*
import com.czyzewski.toUiModel
import com.czyzewski.ui.view.*
import com.czyzewski.userdetails.UserDetailsIntent.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.serialization.ImplicitReflectionSerializer

@ImplicitReflectionSerializer
@InternalCoroutinesApi
class UserDetailsRenderer(
    private val eventHandler: IUserDetailsEventHandler,
    private val navArgs: UserDetailsFragmentArgs
) : IUserDetailsRenderer {

    private lateinit var toolbar: ToolbarView
    private lateinit var progressView: ProgressView
    private lateinit var errorView: ErrorView
    private lateinit var userImage: ImageView
    private lateinit var reposHeaderView: RoundedHeaderView
    private lateinit var gistsHeaderView: RoundedHeaderView
    private lateinit var followersHeaderView: RoundedHeaderView
    private lateinit var nameLabelWithIcon: LabelWithIconView
    private lateinit var locationLabelWithIcon: LabelWithIconView
    private lateinit var companyLabelWithIcon: LabelWithIconView
    private lateinit var twitterLabelWithIcon: LabelWithIconView

    init {
        eventHandler.handle(Init(navArgs.userId, navArgs.userName))
    }

    override fun render(state: UserDetailsState) {
        state.rateLimit?.let {
            toolbar.hideProgress()
            toolbar.setRateLimit(it.toUiModel())
        }

        state.userDetails?.let {
            Glide.with(userImage.context)
                .load(navArgs.avatarUrl)
                .into(userImage)

            reposHeaderView.apply {
                setHeaderText(it.publicRepos)
                setLabelText("Repositories")
            }
            gistsHeaderView.apply {
                setHeaderText(it.publicGists)
                setLabelText("Gists")
            }
            followersHeaderView.apply {
                setHeaderText(it.followers)
                setLabelText("Followers")
            }
            nameLabelWithIcon.setOrHide(R.drawable.ic_person_orange_16, it.name)
            locationLabelWithIcon.setOrHide(R.drawable.ic_location_red_16, it.location)
            companyLabelWithIcon.setOrHide(R.drawable.ic_home_16, it.company)
            twitterLabelWithIcon.setOrHide(R.drawable.ic_twitter_16, it.twitterUsername)
        }

        state.isRateLimitLoading.takeIf { it }?.let { toolbar.showProgress() }

        progressView.isVisible = state.isLoading

        state.errorModel?.let { error ->
            when (error.source) {
                USER_DETAILS -> {
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
                RATE_LIMIT -> {
                    toolbar.hideProgress()
                    toolbar.showError()
                }
                else -> throw IllegalStateException("gohg29-7gyh")
            }
        }
        errorView.isVisible = state.errorModel?.let { it.source == USERS } ?: false
    }

    override fun attach(components: UserDetailsComponents) {
        with(components) {
            this@UserDetailsRenderer.toolbar = toolbar.apply {
                onBackClick { eventHandler.handle(BackPress) }
                onRefreshClick { eventHandler.handle(RefreshPress) }
            }
            this@UserDetailsRenderer.progressView = progressView
            this@UserDetailsRenderer.errorView = errorView
            this@UserDetailsRenderer.userImage = userImage.apply {
                transitionName = navArgs.avatarUrl
                Glide.with(this)
                    .load(navArgs.avatarUrl)
                    .into(this)
            }
            this@UserDetailsRenderer.reposHeaderView = reposHeaderView
            this@UserDetailsRenderer.gistsHeaderView = gistsHeaderView
            this@UserDetailsRenderer.followersHeaderView = followersHeaderView
            this@UserDetailsRenderer.nameLabelWithIcon = nameLabelWithIcon
            this@UserDetailsRenderer.locationLabelWithIcon = locationLabelWithIcon
            this@UserDetailsRenderer.companyLabelWithIcon = companyLabelWithIcon
            this@UserDetailsRenderer.twitterLabelWithIcon = twitterLabelWithIcon
        }
    }

    override fun onConfigurationChanged(orientation: Int) {
        TODO("Not yet implemented")
    }
}
