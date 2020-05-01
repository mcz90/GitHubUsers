package com.czyzewski.userdetails

import com.czyzewski.models.RateLimitModel
import com.czyzewski.models.UserDetailsModel
import com.czyzewski.mvi.ScreenState
import com.czyzewski.mvi.statereplayview.ScreenStateModel
import com.czyzewski.net.error.ErrorModel
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.parse

@ImplicitReflectionSerializer
@Serializable
data class UserDetailsState(
    val userDetails: UserDetailsModel? = null,
    val rateLimit: RateLimitModel? = null,
    val isLoading: Boolean = false,
    val isRateLimitLoading: Boolean = false,
    val errorModel: ErrorModel? = null
) : ScreenState {

    override fun serialize() =
        ScreenStateModel(
            name = this::class.simpleName.toString(),
            data = Json(JsonConfiguration.Stable).stringify(serializer(), this),
            timestamp = System.currentTimeMillis()
        )

    override fun stringify() = Json(JsonConfiguration.Stable).stringify(serializer(), this)

    override fun deserialize(data: String) = Json(JsonConfiguration.Stable).parse(data) as UserDetailsState
}
