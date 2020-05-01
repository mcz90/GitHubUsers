package com.czyzewski.userdetails

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.czyzewski.githubuserslist.R
import kotlinx.android.synthetic.main.activity_main.view.*

class UserDetailsNavigator(private val fragment: Fragment) : IUserDetailsNavigator {

    override fun navigateUp() {
        fragment.findNavController().navigateUp()
    }
}
