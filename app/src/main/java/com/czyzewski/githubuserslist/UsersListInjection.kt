package com.czyzewski.githubuserslist

import androidx.fragment.app.Fragment
import com.czyzewski.githubuserslist.usevase.GetUsersUserCase
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

@InternalCoroutinesApi
val githubUsersModule = module {
    viewModel { (fragment: Fragment) ->
        UsersListViewModel(
            navigator = get(parameters = { parametersOf(fragment) }),
            repository = get(),
            getUsersUserCase = GetUsersUserCase(get())
        )
    }
    factory<IUsersListNavigator> { (fragment: Fragment) ->
        UsersListNavigator(fragment = fragment)
    }
    factory<IUsersListEventHandler> { (viewModel: UsersListViewModel) ->
        UsersListEventHandler(viewModel = viewModel)
    }
    factory { (fragment: Fragment, viewModel: UsersListViewModel) ->
        UsersListRenderer(
            context = fragment.requireContext(),
            eventHandler = get(parameters = { parametersOf(viewModel) })
        )
    }
}