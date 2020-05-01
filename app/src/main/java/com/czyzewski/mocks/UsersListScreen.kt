package com.czyzewski.mocks

import com.czyzewski.githubuserslist.LoadingState
import com.czyzewski.githubuserslist.UsersListState
import com.czyzewski.mvi.statereplayview.ScreenStateModel
import com.czyzewski.net.error.ErrorModel
import com.czyzewski.net.error.ErrorSource
import kotlinx.serialization.ImplicitReflectionSerializer

@ImplicitReflectionSerializer
object UsersListScreen {
    val mocks: List<ScreenStateModel> = listOf(
        ScreenStateModel(
            "Error", UsersListState(
                errorModel = ErrorModel.ApiError(
                    source = ErrorSource.USERS,
                    code = 404,
                    errorMessage = "Error"
                )
            ).stringify(),
            System.currentTimeMillis()
        ),
        ScreenStateModel(
            "Initial loading", UsersListState(
                loadingState = LoadingState.INITIAL
            ).stringify(),
            System.currentTimeMillis()
        ),
        ScreenStateModel(
            "More loading", UsersListState(
                loadingState = LoadingState.MORE
            ).stringify(),
            System.currentTimeMillis()
        )
    )
}
