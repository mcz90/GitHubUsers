package com.czyzewski.net.interceptor

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(request = chain.request().newBuilder().apply {
            addHeader("Content-Type", "application/json; charset=utf-8")
            addHeader("Authorization", Credentials.basic("e6632a20ca1923656ea6", "b71b059c528a9921c8fcdd18cf7909a732288200"))
        }.build())
    }
}
