package com.czyzewski.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.czyzewski.ui.R
import com.czyzewski.ui.models.UserUiModel
import kotlinx.android.synthetic.main.view_user.view.*

class UserView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val repositoriesTextViews: List<TextView>

    private val rotationAnimation: Animation =
        AnimationUtils.loadAnimation(context, R.anim.rotate).apply { fillAfter = true }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_user, this)
        repositoriesTextViews = listOf(firstRepository, secondRepository, thirdRepository)
    }

    fun setUser(uiModel: UserUiModel) {
        userNameView.text = uiModel.userName
        avatarImageView.transitionName = uiModel.avatarUrl
        Glide.with(context)
            .load(uiModel.avatarUrl)
            .placeholder(R.drawable.ic_user_blue_56dp)
            .into(avatarImageView)

        val reposUiModel = uiModel.repositories

        reposUiModel.repositories?.let {
            it.forEachIndexed { index, item ->
                repositoriesTextViews[index].setTextOrHide(item)
            }
        }

        repositoriesProgressBar.isVisible = reposUiModel.isLoading
        synchronizeRepositoriesImageView.isVisible =
            (reposUiModel.isLoading && reposUiModel.isSyncing) || reposUiModel.issueResId != null
        synchronizeRepositoriesImageView.setImageResource(
            if (reposUiModel.isError) R.drawable.ic_sync_problem_red_24dp else R.drawable.ic_sync_blue_32dp
        )

        if (reposUiModel.isLoading && reposUiModel.isSyncing) {
            synchronizeRepositoriesImageView.startAnimation(rotationAnimation)
        }
        if (!(reposUiModel.isLoading && reposUiModel.isSyncing)) {
            rotationAnimation.cancel()
        }

        reposUiModel.issueResId?.let { repositoriesIssueTextView.text = context.getText(it) }
        repositoriesIssueTextView.isVisible = reposUiModel.issueResId != null

        val isEmpty = reposUiModel.repositories?.isEmpty() ?: true
        val showText =
            !isEmpty && !reposUiModel.isError && !reposUiModel.isSyncing && !reposUiModel.isLoading
        showRepositoriesTextViews(showText)
    }

    fun setSynchronizeRepositoriesImageClickListener(onSyncIconClickListener: () -> Unit) {
        synchronizeRepositoriesImageView.setOnClickListener { onSyncIconClickListener() }
    }

    fun setOnUserClickListener(onUserClickListener: (View) -> Unit) {
        cardView.setOnClickListener { onUserClickListener(avatarImageView) }
    }

    private fun showRepositoriesTextViews(showText: Boolean) {
        firstRepository.isInvisible = !showText
        secondRepository.isInvisible = !showText
        thirdRepository.isInvisible = !showText
    }

    private fun TextView.setTextOrHide(textValue: String?) {
        if (!textValue.isNullOrEmpty()) {
            text = "\u00B7 $textValue"
            isInvisible = false
        } else {
            isInvisible = true
        }
    }
}
