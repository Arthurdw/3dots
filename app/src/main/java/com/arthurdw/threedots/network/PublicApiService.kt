package com.arthurdw.threedots.network

import com.arthurdw.threedots.data.objects.LoginData
import com.arthurdw.threedots.data.objects.TokenResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface PublicApiService {
    @POST("/users")
    suspend fun loginOrRegisterUser(@Body data: LoginData): TokenResponse
}
