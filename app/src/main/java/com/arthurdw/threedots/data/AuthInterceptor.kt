package com.arthurdw.threedots.data

import com.arthurdw.threedots.utils.State
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("Authorization", "Bearer ${State.LocalApiToken}")
            .build()

        return chain.proceed(request)
    }
}