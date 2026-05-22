package com.saptrackerdrix.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "tcodes")
data class TCode(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val code: String,
    val purpose: String,
    val module: String = "",
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val favorite: Boolean = false
)