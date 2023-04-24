package com.arthurdw.threedots.data

import com.arthurdw.threedots.objects.User

interface PublicApiService {
    suspend fun loginOrRegister(token: String): String
}

interface ProtectedApiService {
    suspend fun getMe(): User
}

interface Repository {
    suspend fun loginOrRegister(token: String): String
    suspend fun getMe(): User
}

class NetworkRepository(
    private val publicApiService: PublicApiService,
    private val protectedApiService: ProtectedApiService
): Repository {
    override suspend fun loginOrRegister(token: String): String {
        return publicApiService.loginOrRegister(token)
    }

    override suspend fun getMe(): User {
        return protectedApiService.getMe()
    }
}