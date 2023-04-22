package com.arthurdw.threedots.data

import com.arthurdw.threedots.data.objects.User
import com.arthurdw.threedots.data.objects.api.LoginData
import com.arthurdw.threedots.data.objects.api.TokenResponse
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

private class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("Authorization", "Bearer ${Api.token}")
            .build()

        return chain.proceed(request)
    }
}


interface PublicEndpoints {
    @POST("/users")
    suspend fun loginOrRegisterUser(@Body data: LoginData): TokenResponse
}

interface ProtectedEndpoints {
    @GET("/users")
    suspend fun getUser(): User?
}

@OptIn(ExperimentalSerializationApi::class)
object Api {
    val token: String? = null

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl("http://192.168.208.1:8787/v1/")
        .build()

    private val authorizedRetrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl("http://192.168.208.1:8787/v1/")
        .client(OkHttpClient.Builder().addInterceptor(AuthInterceptor()).build())
        .build()

    val public: PublicEndpoints by lazy { retrofit.create(PublicEndpoints::class.java) }
    val protected: ProtectedEndpoints by lazy { authorizedRetrofit.create(ProtectedEndpoints::class.java) }
}
