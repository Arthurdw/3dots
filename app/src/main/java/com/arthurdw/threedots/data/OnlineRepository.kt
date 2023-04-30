package com.arthurdw.threedots.data

import com.arthurdw.threedots.data.objects.BasicStock
import com.arthurdw.threedots.data.objects.ChangeUsername
import com.arthurdw.threedots.data.objects.LoginData
import com.arthurdw.threedots.data.objects.NewsItem
import com.arthurdw.threedots.data.objects.PickedStock
import com.arthurdw.threedots.data.objects.StockDetails
import com.arthurdw.threedots.data.objects.StockStatus
import com.arthurdw.threedots.data.objects.StockWorth
import com.arthurdw.threedots.data.objects.User
import com.arthurdw.threedots.data.objects.toBasicStock
import com.arthurdw.threedots.network.ProtectedApiService
import com.arthurdw.threedots.network.PublicApiService

interface NetworkRepository {
    suspend fun loginOrRegister(token: String): String
    suspend fun getMe(): User
    suspend fun changeUsername(username: String): User
    suspend fun getNews(): List<NewsItem>
    suspend fun getWorth(): List<Float>
    suspend fun getStocks(): List<PickedStock>
    suspend fun getFollowed(): List<BasicStock>
    suspend fun getStockStatus(symbol: String): StockStatus
    suspend fun getStockWorth(symbol: String): StockWorth
    suspend fun getStock(symbol: String): StockDetails
    suspend fun followStock(symbol: String)
    suspend fun unfollowStock(symbol: String)
    suspend fun buyStock(symbol: String, amount: Float)
    suspend fun sellStock(symbol: String, amount: Float)
    suspend fun searchStocks(query: String): List<BasicStock>
}

class OnlineRepository(
    private val publicApiService: PublicApiService,
    private val protectedApiService: ProtectedApiService
) : NetworkRepository {
    override suspend fun loginOrRegister(token: String): String {
        val loginData = LoginData(token = token)
        return publicApiService.loginOrRegisterUser(loginData).token
    }

    override suspend fun getMe(): User = protectedApiService.getMe()
    override suspend fun changeUsername(username: String): User =
        protectedApiService.changeUsername(ChangeUsername(username))
    override suspend fun getNews(): List<NewsItem> = protectedApiService.getNews()
    override suspend fun getWorth(): List<Float> = protectedApiService.getWorth()
    override suspend fun getStocks(): List<PickedStock> = protectedApiService.getStocks()
    override suspend fun getFollowed(): List<BasicStock> = protectedApiService.getFollowed()
    override suspend fun getStockStatus(symbol: String): StockStatus =
        protectedApiService.getStockStatus(symbol)
    override suspend fun getStockWorth(symbol: String): StockWorth =
        protectedApiService.getStockWorth(symbol)

    override suspend fun getStock(symbol: String): StockDetails =
        protectedApiService.getStock(symbol)

    override suspend fun followStock(symbol: String) = protectedApiService.followStock(symbol)
    override suspend fun unfollowStock(symbol: String) = protectedApiService.unfollowStock(symbol)
    override suspend fun buyStock(symbol: String, amount: Float) =
        protectedApiService.buyStock(symbol, amount)

    override suspend fun sellStock(symbol: String, amount: Float) =
        protectedApiService.sellStock(symbol, amount)
    override suspend fun searchStocks(query: String): List<BasicStock> =
        protectedApiService.searchStocks(query).toBasicStock()
}