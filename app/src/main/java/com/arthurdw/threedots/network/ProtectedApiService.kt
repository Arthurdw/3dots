package com.arthurdw.threedots.network

import com.arthurdw.threedots.data.DefaultAppContainer.Companion.PREFIX
import com.arthurdw.threedots.data.objects.BasicStock
import com.arthurdw.threedots.data.objects.PickedStock
import com.arthurdw.threedots.data.objects.NewsItem
import com.arthurdw.threedots.data.objects.StockDetails
import com.arthurdw.threedots.data.objects.StockStatus
import com.arthurdw.threedots.data.objects.StockWorth
import com.arthurdw.threedots.data.objects.User
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

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
    @GET("$PREFIX/stock/{id}/worth")
    suspend fun getStockWorth(@Path("id") id: String): StockWorth
    @PUT("$PREFIX/stock/{id}/follow")
    suspend fun followStock(@Path("id") id: String)
    @DELETE("$PREFIX/stock/{id}/follow")
    suspend fun unfollowStock(@Path("id") id: String)
    @POST("$PREFIX/stock/{id}")
    suspend fun buyStock(@Path("id") id: String, @Query("amount") amount: Float)
    @DELETE("$PREFIX/stock/{id}")
    suspend fun sellStock(@Path("id") id: String, @Query("amount") amount: Float)
}
