package com.czyzewski.net.error

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
sealed class ErrorModel(@Transient open val source: ErrorSource? = null) : Throwable() {

    @Serializable
    data class ApiError(override val source: ErrorSource, val code: Int, val errorMessage: String) :
        ErrorModel(source)

    @Serializable
    data class DatabaseError(override val source: ErrorSource, val errorMessage: String) :
        ErrorModel(source)

    @Serializable
    data class UnhandledError(override val source: ErrorSource) : ErrorModel(source)

    @Serializable
    data class NoInternetError(override val source: ErrorSource) : ErrorModel(source)

    @Serializable
    data class ConnectionLostError(override val source: ErrorSource, val errorMessage: String) :
        ErrorModel(source)
}