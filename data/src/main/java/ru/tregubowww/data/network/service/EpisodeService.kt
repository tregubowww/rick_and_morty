package ru.tregubowww.data.network.service

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.tregubowww.data.network.model.EpisodeDto
import ru.tregubowww.data.network.model.EpisodeDtoResult

interface EpisodeService {

    @GET("episode/")
    suspend fun getEpisodeList(@Query("page") query: Int): EpisodeDto


    @GET("episode/")
    suspend fun getEpisodesFilter(
        @Query("name") name: String?,
        @Query("episode") episode: String?,
        @Query("page") page: Int,
    ): EpisodeDto

    @GET("episode/")
    suspend fun getEpisodesSearch(
        @Query("name") name: String,
        @Query("page") page: Int,
    ): EpisodeDto

    @GET("episode/{id}")
    suspend fun getEpisode(
        @Path("id") id: Long,
    ): EpisodeDtoResult

    @GET("episode/{id}")
    suspend fun getEpisodesByListId(
        @Path("id") id: String,
    ): List<EpisodeDtoResult>

}