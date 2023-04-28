package com.arthurdw.threedots.data

import com.arthurdw.threedots.data.objects.PickedStock
import com.arthurdw.threedots.data.objects.LoginData
import com.arthurdw.threedots.data.objects.NewsItem
import com.arthurdw.threedots.network.ProtectedApiService
import com.arthurdw.threedots.network.PublicApiService
import com.arthurdw.threedots.data.objects.User

interface Repository {
    suspend fun loginOrRegister(token: String): String
    suspend fun getMe(): User
    suspend fun getNews(): List<NewsItem>
    suspend fun getWorth(): List<Float>
    suspend fun getStocks(): List<PickedStock>
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
}