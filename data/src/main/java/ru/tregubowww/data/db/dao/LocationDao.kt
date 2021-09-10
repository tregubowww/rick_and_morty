package ru.tregubowww.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.tregubowww.domain.entity.Location

@Dao
interface LocationDao {
    @Query("SELECT*  FROM locations")
    suspend fun getListLocation(): List<Location>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllLocations(episodes: List<Location>)

    @Query("SELECT * FROM locations WHERE name LIKE :searchQuery OR dimension LIKE :searchQuery")
    suspend fun searchLocation(searchQuery: String): List<Location>

    @Query("SELECT * FROM locations WHERE name LIKE :name AND type LIKE :type  AND dimension LIKE :dimension")
    suspend fun filterLocation(name: String, type: String, dimension: String): List<Location>

    @Query("SELECT * FROM locations WHERE id == :id ")
    suspend fun getLocation(id: Long): Location
}