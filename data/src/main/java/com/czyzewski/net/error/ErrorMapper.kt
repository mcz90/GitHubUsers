package com.czyzewski.net.error

import com.czyzewski.net.NetworkConnectivityManager
import com.czyzewski.net.error.ErrorModel.*
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

interface IErrorMapper {
    fun map(source: ErrorSource, cause: Throwable): ErrorModel
}

class ErrorMapper(private val connectivityManager: NetworkConnectivityManager) : IErrorMapper {

    override fun map(source: ErrorSource, cause: Throwable): ErrorModel {
        // TODO handle db error
        return when (cause) {
            is UnknownHostException,
            is ConnectException,
            is SocketException,
            is SocketTimeoutException,
            is SSLException -> when (connectivityManager.isNetworkAvailable()) {
                true -> NoInternetError(source)
                false -> ConnectionLostError(source, "Connection Lost")
            }
            is HttpException -> mapHttpException(source, cause)
            else -> UnhandledError(source)
        }
    }

    private fun mapHttpException(source: ErrorSource, exception: HttpException): ErrorModel {
        return ApiError(
            source = source,
            code = exception.code(),
            errorMessage = exception.message()
        )
    }
}
