package ru.tregubowww.rick_and_morty.di.module

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.tregubowww.data.network.service.CharacterService
import ru.tregubowww.data.network.service.EpisodeService
import ru.tregubowww.data.network.service.LocationService
import javax.inject.Singleton

@Module
class ApiModule {

    @Provides
    @Singleton
    fun provideCharacterService(retrofit: Retrofit): CharacterService {
        return retrofit.create(CharacterService::class.java)
    }

    @Provides
    @Singleton
    fun provideEpisodeService(retrofit: Retrofit): EpisodeService {
        return retrofit.create(EpisodeService::class.java)
    }

    @Provides
    @Singleton
    fun provideLocationService(retrofit: Retrofit): LocationService {
        return retrofit.create(LocationService::class.java)
    }
}