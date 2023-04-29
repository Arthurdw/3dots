package com.arthurdw.threedots.data

import com.arthurdw.threedots.data.objects.BasicStock
import com.arthurdw.threedots.data.objects.PickedStock
import com.arthurdw.threedots.data.objects.LoginData
import com.arthurdw.threedots.data.objects.NewsItem
import com.arthurdw.threedots.data.objects.StockDetails
import com.arthurdw.threedots.data.objects.StockStatus
import com.arthurdw.threedots.network.ProtectedApiService
import com.arthurdw.threedots.network.PublicApiService
import com.arthurdw.threedots.data.objects.User

interface Repository {
    suspend fun loginOrRegister(token: String): String
    suspend fun getMe(): User
    suspend fun getNews(): List<NewsItem>
    suspend fun getWorth(): List<Float>
    suspend fun getStocks(): List<PickedStock>
    suspend fun getFollowed(): List<BasicStock>
    suspend fun getStockStatus(symbol: String): StockStatus
    suspend fun getStock(symbol: String): StockDetails
    suspend fun followStock(symbol: String)
    suspend fun unfollowStock(symbol: String)
}

class NetworkRepository(
    private val publicApiService: PublicApiService,
    private val protectedApiService: ProtectedApiService
) : Repository {
    override suspend fun loginOrRegister(token: String): String {
        val loginData = LoginData(token = token)
        return publicApiService.loginOrRegisterUser(loginData).token
    }

    override suspend fun getMe(): User {
        return protectedApiService.getMe()
    }

    override suspend fun getNews(): List<NewsItem> {
        return protectedApiService.getNews()
    }

    override suspend fun getWorth(): List<Float> {
        return protectedApiService.getWorth()
    }

    override suspend fun getStocks(): List<PickedStock> {
        return protectedApiService.getStocks()
    }

    override suspend fun getFollowed(): List<BasicStock> {
        return protectedApiService.getFollowed()
    }

    override suspend fun getStockStatus(symbol: String): StockStatus {
        return protectedApiService.getStockStatus(symbol)
    }

    override suspend fun getStock(symbol: String): StockDetails {
        return protectedApiService.getStock(symbol)
    }

    override suspend fun followStock(symbol: String) {
        protectedApiService.followStock(symbol)
    }

    override suspend fun unfollowStock(symbol: String) {
        protectedApiService.unfollowStock(symbol)
    }
}