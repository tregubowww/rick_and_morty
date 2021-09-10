package ru.tregubowww.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "episodes",
)
data class Episode(

    @PrimaryKey val id: Long,
    val name: String,
    @ColumnInfo(name = "air_date") val airDate: String,
    val episode: String,
    val characters: String,
)