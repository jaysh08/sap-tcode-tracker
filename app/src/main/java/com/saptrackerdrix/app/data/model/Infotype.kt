package com.saptrackerdrix.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "infotypes")
data class Infotype(
    @PrimaryKey
    val code: String,
    val name: String,
    val category: String = "",
    val isFavorite: Boolean = false
)