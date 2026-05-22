package com.saptrackerdrix.app.data.repository

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.saptrackerdrix.app.data.model.Infotype
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

@Dao
interface InfotypeDao {
    @Query("SELECT * FROM infotypes ORDER BY code ASC")
    fun getAllInfotypes(): Flow<List<Infotype>>
    
    @Query("SELECT * FROM infotypes WHERE code LIKE '%' || :query || '%' OR name LIKE '%' || :query || '%' ORDER BY code ASC")
    fun searchInfotypes(query: String): Flow<List<Infotype>>
    
    @Query("SELECT * FROM infotypes WHERE isFavorite = 1 ORDER BY code ASC")
    fun getFavorites(): Flow<List<Infotype>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(infotypes: List<Infotype>)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(infotype: Infotype)
    
    @Query("UPDATE infotypes SET isFavorite = :isFavorite WHERE code = :code")
    suspend fun updateFavorite(code: String, isFavorite: Boolean)
    
    @Query("SELECT COUNT(*) FROM infotypes")
    suspend fun getCount(): Int
}

@Database(entities = [TCode::class, Infotype::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tCodeDao(): TCodeDao
    abstract fun infotypeDao(): InfotypeDao
}

private val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS infotypes (code TEXT PRIMARY KEY, name TEXT NOT NULL, category TEXT NOT NULL, isFavorite INTEGER NOT NULL DEFAULT 0)")
    }
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
            )
                .addMigrations(MIGRATION_1_2)
                .build()
            INSTANCE = instance
            instance
        }
    }
}