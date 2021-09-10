package ru.tregubowww.domain.repository

import androidx.paging.PagingSource
import ru.tregubowww.domain.entity.Episode
import ru.tregubowww.domain.query.FilterQueryEpisode

interface EpisodeRepository {
    fun getAllEpisodes(): PagingSource<Int, Episode>
    fun searchEpisode(querySearch: String): PagingSource<Int, Episode>
    suspend fun searchEpisodeFromDb(querySearch: String): List<Episode>

    suspend fun getEpisodeFromNetwork(episodeId: Long): Episode
    suspend fun getEpisodeFromDb(episodeId: Long): Episode
    suspend fun getEpisodeListFromDb(): List<Episode>
    fun filterEpisodes(filterQueryEpisode: FilterQueryEpisode): PagingSource<Int, Episode>
    suspend fun filterEpisodesFromDb(filterQueryEpisode: FilterQueryEpisode): List<Episode>
    suspend fun getEpisodeListById(episodesId: String): List<Episode>
    suspend fun getEpisodeListByIdFromDb(episodesId: String): List<Episode>
}