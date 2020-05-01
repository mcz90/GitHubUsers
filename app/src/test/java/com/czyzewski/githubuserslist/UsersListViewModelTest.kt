package com.czyzewski.githubuserslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.czyzewski.githubuserslist.UsersListIntent.Init
import com.czyzewski.githubuserslist.mappers.UserReducer
import com.czyzewski.net.error.ErrorMapper
import com.czyzewski.usecase.*
import com.czyzewski.usecase.GetRateLimitUseCase.RateLimitData
import com.czyzewski.usecase.GetUsersUseCase.UsersData.Loading
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
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

val mockedNavigator = mockk<IUsersListNavigator>(relaxed = true, relaxUnitFun = true)

@ExperimentalCoroutinesApi
@ImplicitReflectionSerializer
@InternalCoroutinesApi
val mockedErrorMapper = mockk<ErrorMapper>(relaxed = true, relaxUnitFun = true)

@InternalCoroutinesApi
@ImplicitReflectionSerializer
val mockedGetUsersUseCase = mockk<GetUsersUseCase>(relaxed = true, relaxUnitFun = true)

@InternalCoroutinesApi
@ImplicitReflectionSerializer
val mockedGetStoredUsersUseCase = mockk<GetStoredUsersUseCase>(relaxed = true, relaxUnitFun = true)

@ExperimentalCoroutinesApi
val mockedGetReposUseCase = mockk<GetReposUseCase>(relaxed = true, relaxUnitFun = true)

@ExperimentalCoroutinesApi
val mockedSyncUsersReposUseCase = mockk<SyncUsersReposUseCase>(relaxed = true, relaxUnitFun = true)

@ExperimentalCoroutinesApi
val mockedGetRateLimitUseCase = mockk<GetRateLimitUseCase>(relaxed = true, relaxUnitFun = true)

@ObsoleteCoroutinesApi
@FlowPreview
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@ImplicitReflectionSerializer
class UsersListViewModelTest : KoinTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    private val mockedObserver =
        mockk<Observer<UsersListState>>(relaxed = true, relaxUnitFun = true)

    private val viewModel: UsersListViewModel by inject()

    @FlowPreview
    @Before
    fun setUp() {
        Dispatchers.setMain(testCoroutineDispatcher)
        MockKAnnotations.init(this)
        startKoin { modules(testGithubUsersModule) }
        coEvery { mockedGetRateLimitUseCase.getRateLimit() } returns flowOf(RateLimitData.Loading)
        viewModel.getStaste().observeForever(mockedObserver)
    }

    @Test
    fun `Init event serves INITIAL loading state`() = runBlockingTest {
        coEvery { mockedGetUsersUseCase.getUsers(any()) } returns flowOf(Loading(true))
        coEvery { mockedGetRateLimitUseCase.getRateLimit() } returns flowOf(RateLimitData.Loading)

        viewModel.onIntentReceived(Init)

        coVerify {
            mockedGetUsersUseCase.getUsers(first = true)
            mockedObserver.onChanged(UsersListState(loadingState = LoadingState.INITIAL))
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testCoroutineDispatcher.cleanupTestCoroutines()
        stopKoin()
        clearAllMocks()
    }
}

@ObsoleteCoroutinesApi
@FlowPreview
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@ImplicitReflectionSerializer
val testGithubUsersModule = module {
    viewModel {
        UsersListViewModel(
            navigator = mockedNavigator,
            getUsersUseCase = mockedGetUsersUseCase,
            getStoredUsersUseCase = mockedGetStoredUsersUseCase,
            getReposUseCase = mockedGetReposUseCase,
            syncUsersReposUseCase = mockedSyncUsersReposUseCase,
            getRateLimitUseCase = mockedGetRateLimitUseCase,
            reducer = UserReducer(mockedErrorMapper),
            viewLifecycleOwner = mockk()
        )
    }
}
