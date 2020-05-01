package com.czyzewski.userdetails

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.czyzewski.githubuserslist.R
import com.czyzewski.githubuserslist.UsersListFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.serialization.ImplicitReflectionSerializer
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@FlowPreview
@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
@InternalCoroutinesApi
@ImplicitReflectionSerializer
@RunWith(AndroidJUnit4::class)
class UserDetailsFragmentTest {

    @Before
    fun before() {
    }

    @Test
    fun testEventFragment() {
        val fragmentArgs = Bundle().apply {
            putLong("userId", 1)
            putString("userName", "mojombo")
        }
        val scenario = launchFragmentInContainer(fragmentArgs, R.style.AppTheme) {
            UsersListFragment()
        }
        Thread.sleep(3000)
//        scenario.onFragment { fragment: UserDetailsFragment ->
//            fragment.viewModel.getStateLiveData().value = UserDetailsState(isLoading = true)
//            Thread.sleep(1000)
//        }
//        scenario.onFragment { fragment: UserDetailsFragment ->
//            fragment.viewModel.getStateLiveData().value = UserDetailsState(isLoading = false)
//            Thread.sleep(3000)
//        }
    }
}
