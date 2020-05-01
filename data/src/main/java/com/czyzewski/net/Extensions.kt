package com.czyzewski.net

import com.czyzewski.models.RequestQueryModel
import com.czyzewski.models.ResponseModel
import okhttp3.Headers
import retrofit2.HttpException
import retrofit2.Response


fun <Dto> Response<Dto>.handle() =
    if (this.isSuccessful) {
        ResponseModel(
            body = body() ?: throw IllegalStateException("Body should not be null"),
            requestQuery = headers().toRequestQueryModel()
        )
    } else {
        throw HttpException(this)
    }

fun <Dto, Model> ResponseModel<Dto>.toModel(mapFunction: (Dto) -> Model) =
    ResponseModel(
        body = mapFunction(body),
        requestQuery = requestQuery
    )

private fun Headers.toRequestQueryModel() =
    RequestQueryModel(
        sinceParam = this["link"].toSinceParam(),
        allLoaded = this["link"].isAllLoaded()
    )

private fun String?.isAllLoaded(): Boolean {
    return this?.contains("rel=\"next\"")?.not() ?: false
}

private fun String?.toSinceParam(): Long? {
    return this?.takeIf { it.contains("<https://api.github.com/users?since=") }
        ?.substringBefore(">; rel=\"next\"")
        ?.substringAfter("<https://api.github.com/users?since=")
        ?.toLong()
}
