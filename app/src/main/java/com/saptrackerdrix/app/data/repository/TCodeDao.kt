package com.saptrackerdrix.app.data.repository

import android.content.Context
import androidx.room.*
import com.saptrackerdrix.app.data.model.TCode
import kotlinx.coroutines.flow.Flow

@Dao
interface TCodeDao {
    @Query("SELECT * FROM tcodes ORDER BY createdAt DESC")
    fun getAllTCodes(): Flow<List<TCode>>
    
    @Query("SELECT * FROM tcodes WHERE code LIKE '%' || :query || '%' OR purpose LIKE '%' || :query || '%' OR module LIKE '%' || :query || '%' ORDER BY createdAt DESC")
    fun searchTCodes(query: String): Flow<List<TCode>>
    
    @Query("SELECT * FROM tcodes WHERE favorite = 1 ORDER BY createdAt DESC")
    fun getFavorites(): Flow<List<TCode>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTCode(tCode: TCode)
    
    @Update
    suspend fun updateTCode(tCode: TCode)
    
    @Delete
    suspend fun deleteTCode(tCode: TCode)
}

@Database(entities = [TCode::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tCodeDao(): TCodeDao
}

object DatabaseProvider {
    @Volatile
    private var INSTANCE: AppDatabase? = null
    
    fun getInstance(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "sap_tcode_database"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}