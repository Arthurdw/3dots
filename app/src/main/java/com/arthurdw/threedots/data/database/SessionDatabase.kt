package com.arthurdw.threedots.data.database

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.arthurdw.threedots.data.room.SessionToken

@Dao
interface SessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(token: SessionToken)

    @Query("SELECT * FROM sessions LIMIT 1")
    suspend fun getCurrentToken(): SessionToken?

    @Query("DELETE FROM sessions")
    suspend fun deleteAll()
}

@Database(entities = [SessionToken::class], version = 1, exportSchema = false)
abstract class SessionDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao

    companion object {
        @Volatile
        private var INSTANCE: SessionDatabase? = null

        fun getDatabase(context: Context): SessionDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    SessionDatabase::class.java,
                    "session_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}

