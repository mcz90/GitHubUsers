package com.czyzewski.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.czyzewski.ui.R
import com.czyzewski.ui.models.SearchResultUiModel
import kotlinx.android.synthetic.main.view_user.view.*
import kotlinx.android.synthetic.main.view_user.view.avatarImageView
import kotlinx.android.synthetic.main.view_user.view.userNameView
import kotlinx.android.synthetic.main.view_user_search_result.view.*

class UserSearchResultView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val repositoriesTextViews: List<TextView>

    init {
        LayoutInflater.from(context).inflate(R.layout.view_user_search_result, this)
        repositoriesTextViews = listOf(firstRepository, secondRepository, thirdRepository)
    }

    fun setSearchResult(uiModel: SearchResultUiModel) {
        userNameView.text = uiModel.userName
        storedIndicatorImageView.isVisible = uiModel.isStored
        Glide.with(context)
            .load(uiModel.avatarUrl)
            .placeholder(R.drawable.ic_user_blue_56dp)
            .into(avatarImageView)
    }
}
