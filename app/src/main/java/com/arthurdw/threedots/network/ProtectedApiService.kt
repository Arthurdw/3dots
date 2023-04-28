package com.arthurdw.threedots.network

import com.arthurdw.threedots.data.DefaultAppContainer.Companion.PREFIX
import com.arthurdw.threedots.data.objects.PickedStock
import com.arthurdw.threedots.data.objects.NewsItem
import com.arthurdw.threedots.data.objects.User
import retrofit2.http.GET

interface ProtectedApiService {
    @GET("$PREFIX/users/me")
    suspend fun getMe(): User
    @GET("$PREFIX/news")
    suspend fun getNews(): List<NewsItem>
    @GET("$PREFIX/users/me/worth")
    suspend fun getWorth(): List<Float>
    @GET("$PREFIX/users/me/picked")
    suspend fun getStocks(): List<PickedStock>
}
