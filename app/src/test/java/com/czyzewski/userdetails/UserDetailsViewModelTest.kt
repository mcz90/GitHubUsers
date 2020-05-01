package com.czyzewski.userdetails

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.czyzewski.net.dto.RateLimitDto
import com.czyzewski.usecase.GetRateLimitUseCase
import com.czyzewski.usecase.GetRateLimitUseCase.RateLimitData
import com.czyzewski.usecase.GetUserDetailsUseCase
import com.czyzewski.usecase.GetUserDetailsUseCase.UsersDetailsData.Loading
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.ImplicitReflectionSerializer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject

private val mockedNavigator = mockk<UserDetailsNavigator>()
private val mockedGetUserDetailsUseCase = mockk<GetUserDetailsUseCase>()
@ExperimentalCoroutinesApi
val mockedGetRateLimitUseCase = mockk<GetRateLimitUseCase>(relaxed = true, relaxUnitFun = true)

@ImplicitReflectionSerializer
@ExperimentalCoroutinesApi
private val mockedReducer = mockk<UserDetailsReducer>()

@ObsoleteCoroutinesApi
@FlowPreview
@ImplicitReflectionSerializer
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class UserDetailsViewModelTest : KoinTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    private val mockedObserver =
        mockk<Observer<UserDetailsState>>(relaxed = true, relaxUnitFun = true)

    private val viewModel: UserDetailsViewModel by inject()

    @FlowPreview
    @Before
    fun setUp() {
        Dispatchers.setMain(testCoroutineDispatcher)
        MockKAnnotations.init(this)
        startKoin { modules(testUserDetailsViewModel) }
        coEvery { mockedGetRateLimitUseCase.getRateLimit() } returns flowOf(RateLimitData.Loading)
        viewModel.getStateLiveData().observeForever(mockedObserver)
    }

    @Test
    fun `test 1`() = runBlockingTest {
        coEvery { mockedGetUserDetailsUseCase.getUserDetails(any(), any()) } returns flowOf(Loading)
        coEvery { mockedReducer.reduce<UserDetailsState>(any()) } returns UserDetailsState(isLoading = true)
        viewModel.onIntentReceived(UserDetailsIntent.Init(1, "1"))

        coVerify { mockedObserver.onChanged(UserDetailsState(isLoading = true)) }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testCoroutineDispatcher.cleanupTestCoroutines()
        stopKoin()
        clearAllMocks()
    }
}

@FlowPreview
@InternalCoroutinesApi
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@ImplicitReflectionSerializer
val testUserDetailsViewModel = module {
    viewModel {
        UserDetailsViewModel(
            navigator = mockedNavigator,
            getUserDetailsUseCase = mockedGetUserDetailsUseCase,
            reducer = mockedReducer,
            getRateLimitUseCase = mockedGetRateLimitUseCase,
            viewLifecycleOwner = mockk()
        )
    }
}
