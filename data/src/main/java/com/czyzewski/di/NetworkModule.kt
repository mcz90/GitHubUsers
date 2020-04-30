package com.czyzewski.di

import com.czyzewski.mappers.UserMapper
import com.czyzewski.net.UsersApiService
import com.czyzewski.net.RemoteUsersDataSource
import com.czyzewski.net.interceptor.AuthInterceptor
import com.czyzewski.net.mock.MockInterceptor
import com.czyzewski.net.mock.RequestFilter
import com.czyzewski.repository.UsersRepository
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

private fun OkHttpClient.Builder.setupMocks(vararg mocks: Map<RequestFilter, MockResponse>): OkHttpClient.Builder {
    val joinedMocksMap = mocks.fold(
        mapOf<RequestFilter, MockResponse>(),
        { accMap, map -> accMap + map }
    )
    setMocks(joinedMocksMap)
    return this
}

fun OkHttpClient.Builder.setMocks(
    mocks: Map<RequestFilter, MockResponse> = mapOf(),
    trustManagers: Array<TrustManager> = getAllTrustingManagers(),
    socketFactory: SSLSocketFactory = getSslSocketFactory(trustManagers),
    hostnameVerifier: HostnameVerifier = HostnameVerifier { _, _ -> true }
): OkHttpClient.Builder {
    return addInterceptor(MockInterceptor(mocks))
        .sslSocketFactory(socketFactory, trustManagers[0] as X509TrustManager)
        .hostnameVerifier(hostnameVerifier)
}

private fun getSslSocketFactory(trustManagers: Array<TrustManager>): SSLSocketFactory =
    SSLContext.getInstance("SSL").apply {
        init(null, trustManagers, java.security.SecureRandom())
    }.socketFactory

private fun getAllTrustingManagers(): Array<TrustManager> = arrayOf(
    object : X509TrustManager {

        override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()

        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
            // no-op
        }

        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            // no-op
        }
    }
)

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(AuthInterceptor())
            .build()
    }

    single<UsersApiService> {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.github.com")
            .client(get())
            .build()
            .create()
    }

    single { RemoteUsersDataSource(get()) }
    single { UsersRepository(get(), get(), UserMapper()) }
}


fun getStringFromResource(fileName: String): String {
    val classLoader = RequestFilter::class.java.classLoader
    return classLoader?.getResourceAsStream(fileName)?.bufferedReader()?.use { it.readText() }
        ?: throw IllegalArgumentException("Cannot find `$fileName` in resources")
}

fun MockResponse.setContentTypeJson() {
    setHeader("Content-Type", "application/json")
}

fun createMockResponse(code: Int, fileName: String?, isJson: Boolean = true): MockResponse {
    return MockResponse().apply {
        setResponseCode(code)
        if (isJson) {
            setContentTypeJson()
        }
        if (!fileName.isNullOrBlank()) {
            setBody(getStringFromResource(fileName))
        }
    }
}
