package ru.tregubowww.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.tregubowww.data.db.dao.CharacterDao
import ru.tregubowww.data.db.dao.EpisodeDao
import ru.tregubowww.data.db.dao.LocationDao
import ru.tregubowww.domain.entity.Character
import ru.tregubowww.domain.entity.Episode
import ru.tregubowww.domain.entity.Location

@Database(
    entities = [
        Character::class,
        Episode::class,
        Location::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class RickAndMortyDataBase : RoomDatabase() {

    abstract val characterDao: CharacterDao
    abstract val episodeDao: EpisodeDao
    abstract val locationDao: LocationDao

    companion object {
        const val NAME_DB = "rick_and_morty_database"
    }
}