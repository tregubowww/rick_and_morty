package ru.tregubowww.data.repository

import android.util.Log
import androidx.paging.PagingSource
import ru.tregubowww.data.db.dao.EpisodeDao
import ru.tregubowww.data.network.service.EpisodeService
import ru.tregubowww.data.paging_source.episodes.EpisodePagingSource
import ru.tregubowww.data.paging_source.episodes.FilterEpisodePagingSource
import ru.tregubowww.data.paging_source.episodes.SearchEpisodePagingSource
import ru.tregubowww.data.toCharacter
import ru.tregubowww.data.toEpisode
import ru.tregubowww.domain.entity.Episode
import ru.tregubowww.domain.query.FilterQueryEpisode
import ru.tregubowww.domain.repository.EpisodeRepository
import javax.inject.Inject

class EpisodeRepositoryImpl @Inject constructor(
    private val episodeService: EpisodeService,
    private val episodeDao: EpisodeDao,
    private val searchEpisodePagingSource: SearchEpisodePagingSource.Factory,
    private val filterEpisodePagingSource: FilterEpisodePagingSource.Factory,
) : EpisodeRepository {

    override fun getAllEpisodes(): PagingSource<Int, Episode> =
        EpisodePagingSource(episodeService, episodeDao)

    override fun searchEpisode(querySearch: String): PagingSource<Int, Episode> =
        searchEpisodePagingSource.create(querySearch)

    override suspend fun searchEpisodeFromDb(querySearch: String): List<Episode> =
        episodeDao.searchEpisodes("%${querySearch}%")

    override suspend fun getEpisodeFromNetwork(episodeId: Long): Episode =
        episodeService.getEpisode(episodeId).toEpisode()

    override suspend fun getEpisodeFromDb(episodeId: Long): Episode =
        episodeDao.getEpisode(episodeId)

    override suspend fun getEpisodeListFromDb(): List<Episode> =
        episodeDao.getListEpisode()

    override fun filterEpisodes(filterQueryEpisode: FilterQueryEpisode): PagingSource<Int, Episode> =
        filterEpisodePagingSource.create(filterQueryEpisode)

    override suspend fun filterEpisodesFromDb(filterQueryEpisode: FilterQueryEpisode): List<Episode> =
        episodeDao.filterEpisodes(
            name = "%${filterQueryEpisode.name ?: ""}%",
            episode = filterQueryEpisode.episode ?: "%",
        )

    override suspend fun getEpisodeListById(episodesId: String): List<Episode> {
        return if (episodesId.split(", ").size == 1) listOf(episodeService.getEpisode(episodesId.toLong()).toEpisode())
        else episodeService.getEpisodesByListId(episodesId).map { it.toEpisode() }
    }

    override suspend fun getEpisodeListByIdFromDb(episodesId: String): List<Episode> {
        val episodesIdList: List<String> = episodesId.split(", ")
        val episodeList = mutableListOf<Episode>()
        episodesIdList.forEach {if (it.isNotBlank()) episodeList.add(episodeDao.getEpisode(it.toLong())) }
        return episodeList
    }
}