package com.czyzewski.githubuserslist

import androidx.fragment.app.Fragment
import com.czyzewski.githubuserslist.mappers.UserReducer
import com.czyzewski.mvi.staterecorder.StateRecorder
import com.czyzewski.userdetails.UserDetailsState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.serialization.ImplicitReflectionSerializer
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

@ObsoleteCoroutinesApi
@FlowPreview
@ImplicitReflectionSerializer
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
val githubUsersModule = module {
    viewModel { (fragment: Fragment) ->
        UsersListViewModel(
            navigator = get(parameters = { parametersOf(fragment) }),
            getUsersUseCase = get(),
            getReposUseCase = get(),
            syncUsersReposUseCase = get(),
            getStoredUsersUseCase = get(),
            getRateLimitUseCase = get(),
            reducer = UserReducer(get(parameters = { parametersOf(fragment.requireActivity()) })),
            viewLifecycleOwner = fragment
        )
    }
    factory<IUsersListNavigator> { (fragment: Fragment) ->
        UsersListNavigator(fragment = fragment)
    }
    factory<IUsersListEventHandler> { (viewModel: UsersListViewModel) ->
        UsersListEventHandler(viewModel = viewModel)
    }
    factory<IUsersListRenderer> { (viewModel: UsersListViewModel) ->
        UsersListRenderer(
            eventHandler = get(parameters = { parametersOf(viewModel) }),
            adapter = UsersListAdapter()
        )
    }
    factory<IUsersListView> { (viewModel: UsersListViewModel) ->
        UsersListView(renderer = get(parameters = { parametersOf(viewModel) }))
    }
}

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@FlowPreview
@ImplicitReflectionSerializer
val stateRecorderModule = module {
    single {
        StateRecorder
            .Builder(
                appContext = androidApplication(),
                states = listOf(UsersListState::class, UserDetailsState::class)
            )
            .build()
    }
}
