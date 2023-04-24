package com.arthurdw.threedots.network

import com.arthurdw.threedots.objects.User
import retrofit2.http.GET

interface ProtectedApiService {
    @GET("/users/me")
    suspend fun getMe(): User
}
