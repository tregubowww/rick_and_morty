package ru.tregubowww.rick_and_morty.di.module

import dagger.Binds
import dagger.Module
import ru.tregubowww.domain.interactor.CharactersInteractor
import ru.tregubowww.domain.interactor.CharactersInteractorImpl
import ru.tregubowww.domain.interactor.EpisodesInteractor
import ru.tregubowww.domain.interactor.EpisodesInteractorImpl
import ru.tregubowww.domain.interactor.LocationInteractor
import ru.tregubowww.domain.interactor.LocationInteractorImpl

@Module
interface InteractorModule {

    @Binds
    fun getCharactersInteractor(
        getCharactersInteractorImpl: CharactersInteractorImpl
    ): CharactersInteractor

    @Binds
    fun getEpisodesInteractor(
        getEpisodesInteractorImpl: EpisodesInteractorImpl
    ): EpisodesInteractor

    @Binds
    fun getLocationInteractor(
        getLocationInteractorImpl: LocationInteractorImpl
    ):  LocationInteractor

}
