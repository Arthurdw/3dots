package com.arthurdw.threedots.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

interface AppContainer {
    val repository: Repository
}

@OptIn(ExperimentalSerializationApi::class)
class DefaultAppContainer : AppContainer {
    private val BASE_URL = "http://192.168.208.1:8787/v1/"
    // private val BASE_URL = "https://3dots.xiler.net/api/v1/"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()

    private val authorizedRetrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .client(OkHttpClient.Builder().addInterceptor(AuthInterceptor()).build())
        .build()

    private val protectedApiService: ProtectedApiService by lazy {
        authorizedRetrofit.create(ProtectedApiService::class.java)
    }

    private val publicApiService: PublicApiService by lazy {
        retrofit.create(PublicApiService::class.java)
    }

    override val repository: Repository by lazy {
        NetworkRepository(publicApiService, protectedApiService)
    }
}