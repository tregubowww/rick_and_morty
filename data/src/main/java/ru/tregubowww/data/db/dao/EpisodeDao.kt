package ru.tregubowww.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.tregubowww.domain.entity.Character
import ru.tregubowww.domain.entity.Episode

@Dao
interface EpisodeDao {

    @Query("SELECT*  FROM episodes")
    suspend fun getListEpisode(): List<Episode>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllEpisodes(episodes: List<Episode>)

    @Query("SELECT * FROM episodes WHERE name LIKE :searchQuery OR episode LIKE :searchQuery")
    suspend fun searchEpisodes(searchQuery: String): List<Episode>

    @Query("SELECT * FROM episodes WHERE name LIKE :name AND episode LIKE :episode")
    suspend fun filterEpisodes(name: String, episode: String): List<Episode>

    @Query("SELECT * FROM episodes WHERE id == :id ")
    suspend fun getEpisode(id: Long): Episode
}
