package com.arthurdw.threedots.data.database

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.arthurdw.threedots.data.room.Setting

@Dao
interface SettingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setNotificationEnabled(setting: Setting)

    @Query("SELECT * FROM settings WHERE token = 'notifications_enabled' LIMIT 1")
    suspend fun getNotificationEnabled(): Setting?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setPadlockValue(setting: Setting)

    @Query("SELECT * FROM settings WHERE token = 'padlock_value' LIMIT 1")
    suspend fun getPadlockValue(): Setting?
}

@Database(entities = [Setting::class], version = 1, exportSchema = false)
abstract class SettingDatabase : RoomDatabase() {
    abstract fun settingsDao(): SettingDao

    companion object {
        @Volatile
        private var INSTANCE: SettingDatabase? = null

        fun getDatabase(context: Context): SettingDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    SettingDatabase::class.java,
                    "settings_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}

