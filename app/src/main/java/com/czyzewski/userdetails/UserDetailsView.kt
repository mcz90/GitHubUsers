package com.czyzewski.userdetails

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.serialization.ImplicitReflectionSerializer

@ImplicitReflectionSerializer
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class UserDetailsView(private val renderer: IUserDetailsRenderer) : IUserDetailsView {

    override fun <Components> attach(components: Components) {
        renderer.attach(components as UserDetailsComponents)
    }

    override fun <State> render(state: State) {
        renderer.render(state as UserDetailsState)
    }

    override fun onConfigurationChanged(orientation: Int) {
        TODO("Not yet implemented")
    }
}
