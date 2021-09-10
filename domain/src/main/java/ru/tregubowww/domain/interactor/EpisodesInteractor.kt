package ru.tregubowww.domain.interactor

import android.util.Log
import androidx.paging.PagingSource
import ru.tregubowww.domain.entity.Episode
import ru.tregubowww.domain.query.FilterQueryCharacter
import ru.tregubowww.domain.query.FilterQueryEpisode
import ru.tregubowww.domain.repository.EpisodeRepository
import javax.inject.Inject

interface EpisodesInteractor {
    fun getAllEpisodes(): PagingSource<Int, Episode>
    fun searchEpisode(querySearch: String): PagingSource<Int, Episode>
    suspend fun searchEpisodeFromDb(querySearch: String): List<Episode>

    suspend fun getEpisodeFromNetwork(episodeId: Long): Episode
    suspend fun getEpisodeFromDb(episodeId: Long): Episode
    suspend fun getEpisodeListFromDb(): List<Episode>
    fun filterEpisodes(queryFilterEpisode: FilterQueryEpisode): PagingSource<Int, Episode>
    suspend fun filterEpisodesFromDb(queryFilterEpisode: FilterQueryEpisode): List<Episode>
    suspend fun getEpisodeListById(episodesId: String): List<Episode>
    suspend fun getEpisodeListByIdFromDb(episodesId: String): List<Episode>
}

class EpisodesInteractorImpl @Inject constructor(
    private val episodeRepository: EpisodeRepository,
) : EpisodesInteractor {

    override fun getAllEpisodes(): PagingSource<Int, Episode> =
        episodeRepository.getAllEpisodes()

    override fun searchEpisode(querySearch: String): PagingSource<Int, Episode> =
        episodeRepository.searchEpisode(querySearch)

    override suspend fun searchEpisodeFromDb(querySearch: String) =
        episodeRepository.searchEpisodeFromDb(querySearch)

    override suspend fun getEpisodeFromNetwork(episodeId: Long): Episode =
        episodeRepository.getEpisodeFromNetwork(episodeId)

    override suspend fun getEpisodeFromDb(episodeId: Long): Episode =
        episodeRepository.getEpisodeFromDb(episodeId)

    override suspend fun getEpisodeListFromDb(): List<Episode> =
        episodeRepository.getEpisodeListFromDb()

    override fun filterEpisodes(queryFilterEpisode: FilterQueryEpisode): PagingSource<Int, Episode> =
        episodeRepository.filterEpisodes(queryFilterEpisode)

    override suspend fun filterEpisodesFromDb(queryFilterEpisode: FilterQueryEpisode): List<Episode> =
        episodeRepository.filterEpisodesFromDb(queryFilterEpisode)

    override suspend fun getEpisodeListById(episodesId: String): List<Episode> =
        episodeRepository.getEpisodeListById(episodesId)

    override suspend fun getEpisodeListByIdFromDb(episodesId: String): List<Episode> {
       return episodeRepository.getEpisodeListByIdFromDb(episodesId)
    }
}
