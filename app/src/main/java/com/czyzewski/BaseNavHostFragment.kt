package com.czyzewski

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.plusAssign
import com.czyzewski.githubuserslist.R

class BaseNavHostFragment : NavHostFragment() {

    override fun onCreateNavController(navController: NavController) {
        super.onCreateNavController(navController)
        navController.navigatorProvider += BaseFragmentNavigator(
            requireContext(),
            childFragmentManager,
            id
        )
    }

    @Navigator.Name("fragment")
    internal inner class BaseFragmentNavigator(
        context: Context,
        manager: FragmentManager,
        containerId: Int
    ) : FragmentNavigator(context, manager, containerId) {

        private val defaultNavOptions = NavOptions.Builder()
            .setEnterAnim(R.anim.nav_default_enter_anim)
            .setExitAnim(R.anim.nav_default_exit_anim)
            .setPopEnterAnim(R.anim.nav_default_pop_enter_anim)
            .setPopExitAnim(R.anim.nav_default_pop_exit_anim)
            .build()

        override fun navigate(
            destination: Destination,
            args: Bundle?,
            navOptions: NavOptions?,
            navigatorExtras: Navigator.Extras?
        ): NavDestination? {
            return super.navigate(destination, args, defaultNavOptions, navigatorExtras)
        }
    }
}
