package ru.tregubowww.rick_and_morty.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.tregubowww.rick_and_morty.character.characters_search.CharacterSearchViewModel
import ru.tregubowww.rick_and_morty.di.ViewModelFactory
import ru.tregubowww.rick_and_morty.character.characters.CharactersViewModel
import ru.tregubowww.rick_and_morty.di.key.ViewModelKey
import ru.tregubowww.rick_and_morty.episode.episode_search.EpisodeSearchViewModel
import ru.tregubowww.rick_and_morty.episode.episodes.EpisodesViewModel
import ru.tregubowww.rick_and_morty.location.loactions_search.LocationSearchViewModel
import ru.tregubowww.rick_and_morty.location.locations.LocationsViewModel

@Module
interface ViewModelModule {

    @Binds
    fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CharactersViewModel::class)
    fun bindCharacterListViewModel(charactersViewModel: CharactersViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CharacterSearchViewModel::class)
    fun bindCharacterSearchViewModel(charactersViewModel: CharacterSearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LocationsViewModel::class)
    fun bindLocationsViewModel(locationsViewModel: LocationsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LocationSearchViewModel::class)
    fun bindLocationSearchViewModel(locationSearchViewModel: LocationSearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EpisodesViewModel::class)
    fun bindEpisodesViewModel(episodesViewModel: EpisodesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EpisodeSearchViewModel::class)
    fun bindEpisodeSearchViewModel(episodeSearchViewModel: EpisodeSearchViewModel): ViewModel
}
