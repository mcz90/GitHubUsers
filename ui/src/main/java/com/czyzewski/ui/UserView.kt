package com.czyzewski.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.czyzewski.ui.models.RepositoriesUi
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
        Glide.with(context)
            .load(uiModel.avatarUrl)
            .placeholder(R.drawable.ic_user_blue_56dp)
            .into(avatarImageView)

        when (val repositories = uiModel.repositories) {
            RepositoriesUi.Loading -> handleRepositoriesLoading()
            is RepositoriesUi.Loaded -> handleRepositoriesSuccess(repositories.data.repositories)
            is RepositoriesUi.Empty -> handleRepositoriesEmpty(repositories.issueResId)
            is RepositoriesUi.Error -> handleRepositoriesError(repositories.issueResId)
            RepositoriesUi.Syncing -> handleRepositoriesSyncing()
            is RepositoriesUi.SyncSuccess -> handleRepositoriesSynchronization(repositories.data.repositories)
            is RepositoriesUi.SyncError -> handleRepositoriesSynchronizationError(repositories.issueResId)
        }
    }

    fun setSynchronizeRepositoriesImageClickListener(onSyncIconClickListener: () -> Unit) {
        synchronizeRepositoriesImageView.setOnClickListener { onSyncIconClickListener() }
    }


    private fun handleRepositoriesSuccess(repositories: List<String>) {
        repositoriesIssueTextView.isVisible = false
        repositoriesProgressBar.isVisible = false
        synchronizeRepositoriesImageView.isVisible = false
        repositories.forEachIndexed { index, item ->
            repositoriesTextViews[index].setTextOrHide(item)
        }
    }

    private fun handleRepositoriesSynchronization(repositories: List<String>) {
        handleRepositoriesSuccess(repositories)
        rotationAnimation.cancel()
    }

    private fun handleRepositoriesSynchronizationError(@StringRes issueResId: Int) {
        repositoriesIssueTextView.text = context.getText(issueResId)
        repositoriesIssueTextView.isVisible = true
        repositoriesProgressBar.isVisible = false
        synchronizeRepositoriesImageView.isVisible = true
        synchronizeRepositoriesImageView.setImageResource(R.drawable.ic_sync_problem_red_24dp)
        rotationAnimation.cancel()
        hideRepositoriesTextViews()
    }

    private fun handleRepositoriesError(@StringRes issueResId: Int) {
        repositoriesIssueTextView.text = context.getText(issueResId)
        repositoriesIssueTextView.isVisible = true
        repositoriesProgressBar.isVisible = false
        synchronizeRepositoriesImageView.isVisible = true
        synchronizeRepositoriesImageView.setImageResource(R.drawable.ic_sync_problem_red_24dp)
        rotationAnimation.cancel()
        hideRepositoriesTextViews()
    }

    private fun handleRepositoriesEmpty(@StringRes issueResId: Int) {
        repositoriesIssueTextView.text = context.getText(issueResId)
        repositoriesIssueTextView.isVisible = true
        repositoriesProgressBar.isVisible = false
        synchronizeRepositoriesImageView.isVisible = true
        synchronizeRepositoriesImageView.setImageResource(R.drawable.ic_sync_blue_32dp)
        rotationAnimation.cancel()
        hideRepositoriesTextViews()
    }

    private fun handleRepositoriesLoading() {
        repositoriesIssueTextView.isVisible = false
        repositoriesProgressBar.isVisible = true
        synchronizeRepositoriesImageView.isVisible = false
        hideRepositoriesTextViews()
    }

    private fun handleRepositoriesSyncing() {
        repositoriesIssueTextView.isVisible = false
        repositoriesProgressBar.isVisible = true
        synchronizeRepositoriesImageView.isVisible = true
        synchronizeRepositoriesImageView.startAnimation(rotationAnimation)
        hideRepositoriesTextViews()
    }

    private fun hideRepositoriesTextViews() {
        firstRepository.isInvisible = true
        secondRepository.isInvisible = true
        thirdRepository.isInvisible = true
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
