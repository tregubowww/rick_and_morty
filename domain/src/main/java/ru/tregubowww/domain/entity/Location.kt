package ru.tregubowww.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "locations",
)
data class Location(

    @PrimaryKey val id: Long,
    val name: String,
    val type: String,
    val dimension: String,
    val residents: String,
)
