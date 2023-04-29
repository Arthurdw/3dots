package com.arthurdw.threedots.network

import com.arthurdw.threedots.data.DefaultAppContainer.Companion.PREFIX
import com.arthurdw.threedots.data.objects.BasicStock
import com.arthurdw.threedots.data.objects.PickedStock
import com.arthurdw.threedots.data.objects.NewsItem
import com.arthurdw.threedots.data.objects.StockDetails
import com.arthurdw.threedots.data.objects.StockStatus
import com.arthurdw.threedots.data.objects.User
import retrofit2.http.GET
import retrofit2.http.Path

interface ProtectedApiService {
    @GET("$PREFIX/users/me")
    suspend fun getMe(): User
    @GET("$PREFIX/news")
    suspend fun getNews(): List<NewsItem>
    @GET("$PREFIX/users/me/worth")
    suspend fun getWorth(): List<Float>
    @GET("$PREFIX/users/me/picked")
    suspend fun getStocks(): List<PickedStock>
    @GET("$PREFIX/users/me/followed")
    suspend fun getFollowed(): List<BasicStock>
    @GET("$PREFIX/stock/{id}")
    suspend fun getStock(@Path("id") id: String): StockDetails
    @GET("$PREFIX/stock/{id}/status")
    suspend fun getStockStatus(@Path("id") id: String): StockStatus
}
