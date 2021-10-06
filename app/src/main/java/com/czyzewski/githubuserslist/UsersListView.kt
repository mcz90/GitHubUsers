package com.czyzewski.githubuserslist

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.serialization.ImplicitReflectionSerializer

@ImplicitReflectionSerializer
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class UsersListView(private val renderer: IUsersListRenderer) : IUsersListView {

    override fun <Components> attach(components: Components) {
        renderer.attach(components as UserListComponents)
    }

    override fun <State> render(state: State) {
        renderer.render(state as UsersListState)
    }

    override fun onConfigurationChanged(orientation: Int) {
        renderer.onConfigurationChanged(orientation)
    }

    override fun detach() {

    }
}
