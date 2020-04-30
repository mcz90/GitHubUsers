package com.czyzewski.githubuserslist

import android.content.Context
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.czyzewski.models.Repositories
import com.czyzewski.models.RepositoriesModel
import com.czyzewski.models.UserModel
import com.czyzewski.ui.UserView
import com.czyzewski.ui.models.RepositoriesUi
import com.czyzewski.ui.models.RepositoriesUiModel
import com.czyzewski.ui.models.UserUiModel

class UsersListAdapter(private val context: Context) : Adapter<UsersListAdapter.UsersViewHolder>() {

    private val userList: MutableList<UserModel> = mutableListOf()
    private var onSyncIconClickListener: ((Pair<String, Long>) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view = UserView(context).apply {
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        }
        return UsersViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bind(userList[position], position)
    }

    override fun getItemCount(): Int {
        return userList.size
    }


    fun setList(users: List<UserModel>) {
        userList.addAll(users)
        notifyDataSetChanged()
    }

    fun repositoriesEmpty(userId: Long, issueResId: Int) {
        val user = userList.first { it.id == userId }
        user.repositories = Repositories.Empty(issueResId)
        notifyItemChanged(userList.indexOf(user))
    }

    fun repositoriesLoadingStarted(userId: Long) {
        val user = userList.first { it.id == userId }
        user.repositories = Repositories.Loading
        notifyItemChanged(userList.indexOf(user))
    }

    fun repositoriesSyncingStarted(userId: Long) {
        val user = userList.first { it.id == userId }
        user.repositories = Repositories.Syncing
        notifyItemChanged(userList.indexOf(user))
    }

    fun repositoriesLoadingError(userId: Long, issueResId: Int) {
        val user = userList.first { it.id == userId }
        user.repositories = Repositories.Error(issueResId)
        notifyItemChanged(userList.indexOf(user))
    }

    fun repositoriesSyncError(userId: Long, @StringRes issueResId: Int) {
        val user = userList.first { it.id == userId }
        user.repositories = Repositories.SyncError(issueResId)
        notifyItemChanged(userList.indexOf(user))
    }

    fun repositoriesLoaded(userId: Long, data: RepositoriesModel) {
        val user = userList.first { it.id == userId }
        user.repositories = Repositories.Loaded(data)
        notifyItemChanged(userList.indexOf(user))
    }

    fun repositoriesSyncSuccess(userId: Long, data: RepositoriesModel) {
        val user = userList.first { it.id == userId }
        user.repositories = Repositories.SyncSuccess(data)
        notifyItemChanged(userList.indexOf(user))
    }

    fun setOnSyncIconClickListener(onSyncIconClickListener: (Pair<String, Long>) -> Unit) {
        this.onSyncIconClickListener = onSyncIconClickListener
    }


    inner class UsersViewHolder(private val view: UserView) : ViewHolder(view) {

        fun bind(model: UserModel, index: Int): Unit =
            with(view) {
                val repositories = when (val repositories = model.repositories) {
                    Repositories.Loading -> RepositoriesUi.Loading
                    is Repositories.Loaded -> RepositoriesUi.Loaded(
                        RepositoriesUiModel(
                            userId = repositories.data.userId,
                            repositories = repositories.data.repositoriesNames
                        )
                    )
                    is Repositories.Empty -> RepositoriesUi.Empty(repositories.issueResId)
                    is Repositories.Error -> RepositoriesUi.Error(repositories.issueResId)
                    Repositories.Syncing -> RepositoriesUi.Syncing
                    is Repositories.SyncSuccess -> RepositoriesUi.SyncSuccess(
                        RepositoriesUiModel(
                            userId = repositories.data.userId,
                            repositories = repositories.data.repositoriesNames
                        )
                    )
                    is Repositories.SyncError -> RepositoriesUi.SyncError(repositories.issueResId)
                }
                setSynchronizeRepositoriesImageClickListener {
                    onSyncIconClickListener?.invoke(
                        model.login to model.id
                    )
                }
                setUser(
                    UserUiModel(
                        model.id,
                        model.login + " $index",
                        model.avatarUrl,
                        repositories
                    )
                )
            }
    }
}
