package com.czyzewski.userdetails

import android.widget.ImageView
import com.czyzewski.mvi.ViewComponents
import com.czyzewski.ui.view.*

data class UserDetailsComponents(
    val toolbar: ToolbarView,
    val progressView: ProgressView,
    val errorView: ErrorView,
    val userImage: ImageView,
    val reposHeaderView: RoundedHeaderView,
    val gistsHeaderView: RoundedHeaderView,
    val followersHeaderView: RoundedHeaderView,
    val nameLabelWithIcon: LabelWithIconView,
    val locationLabelWithIcon: LabelWithIconView,
    val companyLabelWithIcon: LabelWithIconView,
    val twitterLabelWithIcon: LabelWithIconView
) : ViewComponents
