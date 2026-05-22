package com.saptrackerdrix.app.data.dao

import androidx.room.*
import com.saptrackerdrix.app.data.model.Infotype
import kotlinx.coroutines.flow.Flow

@Dao
interface InfotypeDao {
    @Query("SELECT * FROM infotypes ORDER BY code ASC")
    fun getAllInfotypes(): Flow<List<Infotype>>
    
    @Query("SELECT * FROM infotypes WHERE code LIKE '%' || :query || '%' OR name LIKE '%' || :query || '%' ORDER BY code ASC")
    fun searchInfotypes(query: String): Flow<List<Infotype>>
    
    @Query("SELECT * FROM infotypes WHERE isFavorite = 1 ORDER BY code ASC")
    fun getFavorites(): Flow<List<Infotype>>
    
    @Query("SELECT * FROM infotypes WHERE category = :category ORDER BY code ASC")
    fun getByCategory(category: String): Flow<List<Infotype>>
    
    @Query("SELECT DISTINCT category FROM infotypes ORDER BY category ASC")
    fun getAllCategories(): Flow<List<String>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(infotypes: List<Infotype>)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(infotype: Infotype)
    
    @Update
    suspend fun update(infotype: Infotype)
    
    @Query("UPDATE infotypes SET isFavorite = :isFavorite WHERE code = :code")
    suspend fun updateFavorite(code: String, isFavorite: Boolean)
    
    @Query("SELECT COUNT(*) FROM infotypes")
    suspend fun getCount(): Int
}