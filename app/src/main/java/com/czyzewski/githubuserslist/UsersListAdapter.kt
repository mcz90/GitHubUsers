package com.czyzewski.githubuserslist

import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.czyzewski.models.Repositories.Empty
import com.czyzewski.models.Repositories.Error
import com.czyzewski.models.Repositories.Loaded
import com.czyzewski.models.Repositories.Loading
import com.czyzewski.models.RepositoriesModel
import com.czyzewski.models.UserModel
import com.czyzewski.ui.models.RepositoriesUiModel
import com.czyzewski.ui.models.UserUiModel
import com.czyzewski.ui.view.UserView

class UsersListAdapter : Adapter<UsersListAdapter.UsersViewHolder>() {

    private val userList: MutableList<UserModel> = mutableListOf()
    private var onSyncIconClickListener: ((Pair<String, Long>) -> Unit)? = null
    private var onUserClickListener: ((Triple<Long, String, TransitionData>) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view = UserView(parent.context).apply {
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

    fun repositoriesLoadingStarted(userId: Long, isSyncing: Boolean) {
        val user = userList.first { it.id == userId }
        user.repositories = Loading(isSyncing)
        notifyItemChanged(userList.indexOf(user))
    }

    fun repositoriesLoadingError(userId: Long) {
        val user = userList.first { it.id == userId }
        user.repositories = Error
        notifyItemChanged(userList.indexOf(user))
    }

    fun repositoriesLoaded(model: RepositoriesModel, isSyncing: Boolean) {
        val user = userList.first { it.id == model.userId }
        user.repositories = if (model.repositories.isNotEmpty()) {
            Loaded(model, isSyncing)
        } else {
            Empty
        }
        notifyItemChanged(userList.indexOf(user))
    }

    fun setOnUserClickListener(onUserClickListener: (Triple<Long, String, TransitionData>) -> Unit) {
        this.onUserClickListener = onUserClickListener
    }

    fun setOnSyncIconClickListener(onSyncIconClickListener: (Pair<String, Long>) -> Unit) {
        this.onSyncIconClickListener = onSyncIconClickListener
    }

    inner class UsersViewHolder(private val view: UserView) : ViewHolder(view) {

        fun bind(model: UserModel, index: Int): Unit =
            with(view) {

                var repoUiModel = RepositoriesUiModel(userId = model.id)
                repoUiModel = when (val repositories = model.repositories) {
                    is Loading -> repoUiModel.copy(
                        isLoading = true,
                        isSyncing = repositories.isSyncing
                    )
                    is Loaded -> repoUiModel.copy(
                        repositories = repositories.data.repositories.map { it.name },
                        isSyncing = repositories.isSyncing
                    )
                    is Empty -> repoUiModel.copy(
                        issueResId = R.string.users_list_users_repositories_empty
                    )
                    is Error -> repoUiModel.copy(
                        isError = true,
                        issueResId = R.string.users_list_users_repositories_error
                    )
                }

                setSynchronizeRepositoriesImageClickListener {
                    onSyncIconClickListener?.invoke(model.login to model.id)
                }

                setOnUserClickListener {
                    onUserClickListener?.invoke(
                        Triple(
                            model.id,
                            model.login,
                            TransitionData(it, model.avatarUrl)
                        )
                    )
                }

                setUser(
                    UserUiModel(
                        model.id,
                        model.login + " $index",
                        model.avatarUrl,
                        repoUiModel
                    )
                )
            }
    }
}
