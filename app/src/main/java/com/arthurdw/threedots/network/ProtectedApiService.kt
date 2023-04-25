package com.arthurdw.threedots.network

import com.arthurdw.threedots.data.DefaultAppContainer.Companion.PREFIX
import com.arthurdw.threedots.objects.User
import retrofit2.http.GET

interface ProtectedApiService {
    @GET("$PREFIX/users/me")
    suspend fun getMe(): User
}
