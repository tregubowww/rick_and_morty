package ru.tregubowww.rick_and_morty.navigation

import com.github.terrakok.cicerone.Router
import ru.tregubowww.domain.query.FilterQueryCharacter
import ru.tregubowww.domain.query.FilterQueryEpisode
import ru.tregubowww.domain.query.FilterQueryLocation
import javax.inject.Inject

class Navigation @Inject constructor(
    private val router: Router,
) {

    fun characters() {
        router.newRootScreen(Screens.characters())
    }

    fun searchCharacter() {
        router.navigateTo(Screens.charactersSearch())
    }

    fun charactersFilterChoice() {
        router.navigateTo(Screens.charactersFilterChoice())
    }

    fun charactersFilter(filterQueryCharacter: FilterQueryCharacter) {
        router.replaceScreen(Screens.charactersFilter(filterQueryCharacter))
    }

    fun characterDetails(id: Long) {
        router.navigateTo(Screens.characterDetails(id))
    }

    fun episodes() {
        router.newRootScreen(Screens.episodes())
    }

    fun searchEpisodes() {
        router.navigateTo(Screens.episodesSearch())
    }

    fun episodesFilterChoice() {
        router.navigateTo(Screens.episodesFilterChoice())
    }

    fun episodesFilter(filterQueryEpisode: FilterQueryEpisode) {
        router.replaceScreen(Screens.episodesFilter(filterQueryEpisode))
    }

    fun episodeDetails(id: Long) {
        router.navigateTo(Screens.episodeDetails(id))
    }

    fun locations() {
        router.newRootScreen(Screens.locations())
    }

    fun searchLocations() {
        router.navigateTo(Screens.locationsSearch())
    }

    fun locationsFilterChoice() {
        router.navigateTo(Screens.locationsFilterChoice())
    }

    fun locationsFilter(filterQueryLocation: FilterQueryLocation) {
        router.replaceScreen(Screens.locationsFilter(filterQueryLocation))
    }

    fun locationDetails(id: Long) {
        router.navigateTo(Screens.locationDetails(id))
    }

    fun back() {
        router.exit()
    }
}
