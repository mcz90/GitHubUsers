package com.czyzewski.userdetails

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.serialization.ImplicitReflectionSerializer
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

@FlowPreview
@InternalCoroutinesApi
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@ImplicitReflectionSerializer
val userDetailsModule = module {
    viewModel { (fragment: Fragment) ->
        UserDetailsViewModel(
            navigator = get(parameters = { parametersOf(fragment) }),
            getUserDetailsUseCase = get(),
            reducer = UserDetailsReducer(get(parameters = { parametersOf(fragment.requireActivity()) })),
            getRateLimitUseCase = get(),
            viewLifecycleOwner = fragment
        )
    }
    factory<IUserDetailsNavigator> { (fragment: Fragment) ->
        UserDetailsNavigator(fragment = fragment)
    }
    factory<IUserDetailsEventHandler> { (viewModel: UserDetailsViewModel) ->
        UserDetailsEventHandler(viewModel = viewModel)
    }
    factory<IUserDetailsRenderer> { (navArgs: UserDetailsFragmentArgs, viewModel: UserDetailsViewModel) ->
        UserDetailsRenderer(
            eventHandler = get(parameters = { parametersOf(viewModel) }),
            navArgs = navArgs
        )
    }
    factory<IUserDetailsView> { (fragment: Fragment, viewModel: UserDetailsViewModel) ->
        val navArgs: UserDetailsFragmentArgs by fragment.navArgs()
        UserDetailsView(renderer = get(parameters = { parametersOf(navArgs, viewModel) }))
    }
}
