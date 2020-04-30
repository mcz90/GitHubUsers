package com.czyzewski.mappers

import com.czyzewski.mappers.ErrorModel.ApiError
import com.czyzewski.mappers.ErrorModel.DatabaseError
import okhttp3.ResponseBody

sealed class ErrorModel {

    data class ApiError(
        val code: Int,
        val message: String,
        val body: ResponseBody?
    ) : ErrorModel()

    data class DatabaseError(val cause: Throwable) : ErrorModel()
}

interface IErrorMapper {
    fun mapRemote(
        code: Int,
        message: String,
        body: ResponseBody
    ): ApiError

    fun mapLocal(cause: Throwable): DatabaseError
}

class ErrorMapper : IErrorMapper {

    override fun mapRemote(
        code: Int,
        message: String,
        body: ResponseBody
    ): ApiError {
        return ApiError(
            code = code,
            message = message,
            body = body
        )
    }

    override fun mapLocal(cause: Throwable): DatabaseError {
        return DatabaseError(cause)
    }
}