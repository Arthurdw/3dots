package com.arthurdw.threedots.data

import android.content.Context
import com.arthurdw.threedots.data.database.SessionDatabase
import com.arthurdw.threedots.data.database.SettingDatabase
import com.arthurdw.threedots.network.ProtectedApiService
import com.arthurdw.threedots.network.PublicApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

interface AppContainer {
    val networkRepository: NetworkRepository
    val offlineRepository: OfflineRepository
}

@OptIn(ExperimentalSerializationApi::class)
class DefaultAppContainer(private val context: Context) : AppContainer {
    companion object {
        const val BASE_URL = "https://3dots.xiler.net"
        const val PREFIX = "/api/v1"
    }

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

    override val networkRepository: NetworkRepository by lazy {
        OnlineRepository(publicApiService, protectedApiService)
    }

    override val offlineRepository: OfflineRepository by lazy {
        OfflineRepository(
            SessionDatabase.getDatabase(context).sessionDao(),
            SettingDatabase.getDatabase(context).settingsDao()
        )
    }
}