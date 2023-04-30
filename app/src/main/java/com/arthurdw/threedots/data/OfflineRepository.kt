package com.arthurdw.threedots.data

import com.arthurdw.threedots.data.database.SessionDao
import com.arthurdw.threedots.data.database.SettingDao
import com.arthurdw.threedots.data.room.SessionToken
import com.arthurdw.threedots.data.room.Setting

interface LocalRepository {
    suspend fun getSessionToken(): String?
    suspend fun setSessionToken(token: String)
    suspend fun deleteSessionToken()
    suspend fun notificationsEnabled(): Boolean
    suspend fun setNotificationsEnabled(enabled: Boolean)
    suspend fun getPadlockValue(): String?
    suspend fun setPadlockValue(value: String)
}

class OfflineRepository(
    private val sessionDao: SessionDao,
    private val settingDao: SettingDao
    ): LocalRepository {
    override suspend fun getSessionToken(): String? {
        return sessionDao.getCurrentToken()?.token
    }

    override suspend fun setSessionToken(token: String) {
        // Only one token can be stored at a time
        sessionDao.deleteAll()
        sessionDao.insert(SessionToken(token))
    }

    override suspend fun deleteSessionToken() {
        sessionDao.deleteAll()
    }

    override suspend fun notificationsEnabled(): Boolean {
        return settingDao.getNotificationEnabled()?.value?.toBoolean() ?: true
    }

    override suspend fun setNotificationsEnabled(enabled: Boolean) {
        settingDao.setNotificationEnabled(Setting("notifications_enabled", enabled.toString()))
    }

    override suspend fun getPadlockValue(): String? {
        return settingDao.getPadlockValue()?.value
    }

    override suspend fun setPadlockValue(value: String) {
        settingDao.setPadlockValue(Setting("padlock_value", value))
    }
}