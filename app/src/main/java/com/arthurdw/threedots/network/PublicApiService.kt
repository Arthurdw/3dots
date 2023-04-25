package com.arthurdw.threedots.network

import com.arthurdw.threedots.data.DefaultAppContainer.Companion.PREFIX
import com.arthurdw.threedots.data.objects.LoginData
import com.arthurdw.threedots.data.objects.TokenResponse
import retrofit2.http.Body
import retrofit2.http.POST


interface PublicApiService {
    @POST("$PREFIX/users")
    suspend fun loginOrRegisterUser(@Body data: LoginData): TokenResponse
}
