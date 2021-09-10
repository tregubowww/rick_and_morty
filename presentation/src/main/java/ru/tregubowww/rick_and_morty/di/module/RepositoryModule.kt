package ru.tregubowww.rick_and_morty.di.module

import dagger.Binds
import dagger.Module
import ru.tregubowww.data.repository.CharacterRepositoryImpl
import ru.tregubowww.data.repository.EpisodeRepositoryImpl
import ru.tregubowww.data.repository.LocationRepositoryImpl
import ru.tregubowww.domain.repository.CharacterRepository
import ru.tregubowww.domain.repository.EpisodeRepository
import ru.tregubowww.domain.repository.LocationRepository

@Module
interface RepositoryModule {

    @Binds
    fun characterRepository(
        characterRepositoryImpl: CharacterRepositoryImpl
    ): CharacterRepository

    @Binds
    fun EpisodeRepository(
        characterDetailsRepositoryImpl: EpisodeRepositoryImpl
    ): EpisodeRepository

    @Binds
    fun locationRepository(
        characterDetailsRepositoryImpl: LocationRepositoryImpl
    ): LocationRepository
}