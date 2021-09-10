package ru.tregubowww.rick_and_morty.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.tregubowww.domain.query.FilterQueryCharacter
import ru.tregubowww.domain.query.FilterQueryEpisode
import ru.tregubowww.domain.query.FilterQueryLocation
import ru.tregubowww.rick_and_morty.character.character_details.CharacterDetailsFragment
import ru.tregubowww.rick_and_morty.character.characters.CharactersFragment
import ru.tregubowww.rick_and_morty.character.characters_filter.CharacterFilterChoiceFragment
import ru.tregubowww.rick_and_morty.character.characters_filter.CharacterFilterFragment
import ru.tregubowww.rick_and_morty.character.characters_search.CharacterSearchFragment
import ru.tregubowww.rick_and_morty.episode.episodes_filter.EpisodeFilterChoiceFragment
import ru.tregubowww.rick_and_morty.episode.episodes_filter.EpisodeFilterFragment
import ru.tregubowww.rick_and_morty.episode.episode_details.EpisodeDetailsFragment
import ru.tregubowww.rick_and_morty.episode.episode_search.EpisodeSearchFragment
import ru.tregubowww.rick_and_morty.episode.episodes.EpisodesFragment
import ru.tregubowww.rick_and_morty.location.loactions_search.LocationSearchFragment
import ru.tregubowww.rick_and_morty.location.location_details.LocationDetailsFragment
import ru.tregubowww.rick_and_morty.location.locations.LocationsFragment
import ru.tregubowww.rick_and_morty.location.locations_filter.LocationFilterChoiceFragment
import ru.tregubowww.rick_and_morty.location.locations_filter.LocationFilterFragment

object Screens {
    fun characters() = FragmentScreen { CharactersFragment() }
    fun charactersSearch() = FragmentScreen { CharacterSearchFragment.newInstance() }
    fun charactersFilterChoice() = FragmentScreen { CharacterFilterChoiceFragment.newInstance() }
    fun characterDetails(id: Long) = FragmentScreen { CharacterDetailsFragment.newInstance(id)}
    fun charactersFilter(filterQueryCharacter: FilterQueryCharacter) = FragmentScreen { CharacterFilterFragment.newInstance(filterQueryCharacter) }

    fun episodes() = FragmentScreen { EpisodesFragment() }
    fun episodesSearch() = FragmentScreen { EpisodeSearchFragment.newInstance() }
    fun episodesFilterChoice() = FragmentScreen { EpisodeFilterChoiceFragment.newInstance() }
    fun episodesFilter(filterQueryEpisode: FilterQueryEpisode) = FragmentScreen { EpisodeFilterFragment.newInstance(filterQueryEpisode) }
    fun episodeDetails(id: Long) = FragmentScreen { EpisodeDetailsFragment.newInstance(id) }

    fun locations() = FragmentScreen { LocationsFragment() }
    fun locationsSearch() = FragmentScreen { LocationSearchFragment.newInstance() }
    fun locationsFilterChoice() = FragmentScreen { LocationFilterChoiceFragment.newInstance() }
    fun locationsFilter(filterQueryLocation: FilterQueryLocation) = FragmentScreen { LocationFilterFragment.newInstance(filterQueryLocation) }
    fun locationDetails(id: Long) = FragmentScreen { LocationDetailsFragment.newInstance(id) }
}
