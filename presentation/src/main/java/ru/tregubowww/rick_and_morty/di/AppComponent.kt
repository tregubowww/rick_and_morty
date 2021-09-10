package ru.tregubowww.rick_and_morty.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.tregubowww.rick_and_morty.MainActivity
import ru.tregubowww.rick_and_morty.character.character_details.CharacterDetailsFragment
import ru.tregubowww.rick_and_morty.character.characters.CharactersFragment
import ru.tregubowww.rick_and_morty.character.characters_filter.CharacterFilterChoiceFragment
import ru.tregubowww.rick_and_morty.character.characters_filter.CharacterFilterFragment
import ru.tregubowww.rick_and_morty.character.characters_search.CharacterSearchFragment
import ru.tregubowww.rick_and_morty.di.module.ApiModule
import ru.tregubowww.rick_and_morty.di.module.DataBaseModule
import ru.tregubowww.rick_and_morty.di.module.InteractorModule
import ru.tregubowww.rick_and_morty.di.module.NavigationModule
import ru.tregubowww.rick_and_morty.di.module.NetworkModule
import ru.tregubowww.rick_and_morty.di.module.RepositoryModule
import ru.tregubowww.rick_and_morty.di.module.ViewModelModule
import ru.tregubowww.rick_and_morty.episode.episode_details.EpisodeDetailsFragment
import ru.tregubowww.rick_and_morty.episode.episode_search.EpisodeSearchFragment
import ru.tregubowww.rick_and_morty.episode.episodes.EpisodesFragment
import ru.tregubowww.rick_and_morty.episode.episodes_filter.EpisodeFilterChoiceFragment
import ru.tregubowww.rick_and_morty.episode.episodes_filter.EpisodeFilterFragment
import ru.tregubowww.rick_and_morty.location.loactions_search.LocationSearchFragment
import ru.tregubowww.rick_and_morty.location.location_details.LocationDetailsFragment
import ru.tregubowww.rick_and_morty.location.locations.LocationsFragment
import ru.tregubowww.rick_and_morty.location.locations_filter.LocationFilterChoiceFragment
import ru.tregubowww.rick_and_morty.location.locations_filter.LocationFilterFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [
    NetworkModule::class,
    ApiModule::class,
    ViewModelModule::class,
    InteractorModule::class,
    DataBaseModule::class,
    RepositoryModule::class,
    NavigationModule::class
])

interface AppComponent {

    fun inject(mainActivity: MainActivity)
    fun inject(characterFilterChoiceFragment: CharacterFilterChoiceFragment)
    fun inject(characterSearchFragment: CharacterSearchFragment)
    fun inject(charactersFragment: CharactersFragment)
    fun inject(characterDetailsFragment: CharacterDetailsFragment)
    fun inject(characterFilterFragment: CharacterFilterFragment)
    fun inject(locationsFragment: LocationsFragment)
    fun inject(episodesFragment: EpisodesFragment)
    fun inject(locationSearchFragment: LocationSearchFragment)
    fun inject(locationFilterChoiceFragment: LocationFilterChoiceFragment)
    fun inject(locationFilterFragment: LocationFilterFragment)
    fun inject(locationDetailsFragment: LocationDetailsFragment)
    fun inject(episodeFilterChoiceFragment: EpisodeFilterChoiceFragment)
    fun inject(episodeFilterFragment: EpisodeFilterFragment)
    fun inject(episodeSearchFragment: EpisodeSearchFragment)
    fun inject(episodeDetailsFragment: EpisodeDetailsFragment)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}