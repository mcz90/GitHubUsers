package com.czyzewski.githubuserslist

import android.widget.Toast
import androidx.fragment.app.Fragment

class UsersListNavigator(private val fragment : Fragment) : IUsersListNavigator {

    override fun navigateUp() {
        Toast.makeText(fragment.requireContext(), "safdsfds", Toast.LENGTH_SHORT).show()
    }
}