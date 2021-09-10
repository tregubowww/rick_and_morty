package ru.tregubowww.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "characters",
)
data class Character(

    @PrimaryKey val id: Long,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val image: String,
    val type: String,
    @ColumnInfo(name = "origin_name_location") val originNameLocation: String,
    @ColumnInfo(name = "origin_id_location") val originIdLocation: Long?,
    @ColumnInfo(name = "name_location") val nameLocation: String,
    @ColumnInfo(name = "id_location") val idLocation: Long?,
    @ColumnInfo(name = "episodes_id") val episodesId: String,
)