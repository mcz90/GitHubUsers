package com.czyzewski.githubuserslist

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController

class UsersListNavigator(private val fragment: Fragment) : IUsersListNavigator {

    override fun navigateUp() {
        Toast.makeText(fragment.requireContext(), "safdsfds", Toast.LENGTH_SHORT).show()
    }

    override fun navigateToUserDetailsScreen(
        userId: Long,
        userName: String,
        transitionData: TransitionData
    ) {
        val extras = FragmentNavigatorExtras(transitionData.view to transitionData.avatarUrl)
        val destination = UsersListFragmentDirections.actionUsersListToUserDetails(userId, userName, transitionData.avatarUrl)
        fragment.findNavController().navigate(destination, extras)
    }
}

data class TransitionData(val view: View, val avatarUrl: String)
