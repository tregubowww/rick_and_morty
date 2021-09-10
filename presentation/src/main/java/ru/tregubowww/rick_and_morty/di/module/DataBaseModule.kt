package ru.tregubowww.rick_and_morty.di.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.tregubowww.data.db.dao.CharacterDao
import ru.tregubowww.data.db.RickAndMortyDataBase
import ru.tregubowww.data.db.dao.EpisodeDao
import ru.tregubowww.data.db.dao.LocationDao
import javax.inject.Singleton


@Module
class DataBaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): RickAndMortyDataBase {
        return Room.databaseBuilder(context, RickAndMortyDataBase::class.java, RickAndMortyDataBase.NAME_DB)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideCharacterDao(database: RickAndMortyDataBase): CharacterDao {
        return database.characterDao
    }

    @Provides
    @Singleton
    fun provideEpisodeDao(database: RickAndMortyDataBase): EpisodeDao {
        return database.episodeDao
    }

    @Provides
    @Singleton
    fun provideLocationDao(database: RickAndMortyDataBase): LocationDao {
        return database.locationDao
    }
}


