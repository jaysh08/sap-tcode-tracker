package com.saptrackerdrix.app.data.repository

import com.saptrackerdrix.app.data.model.TCode
import kotlinx.coroutines.flow.Flow

interface TCodeRepository {
    fun getAllTCodes(): Flow<List<TCode>>
    fun searchTCodes(query: String): Flow<List<TCode>>
    fun getFavorites(): Flow<List<TCode>>
    suspend fun insertTCode(tCode: TCode)
    suspend fun updateTCode(tCode: TCode)
    suspend fun deleteTCode(tCode: TCode)
    suspend fun toggleFavorite(tCode: TCode)
}