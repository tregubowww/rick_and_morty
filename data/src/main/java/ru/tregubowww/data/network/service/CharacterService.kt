package ru.tregubowww.data.network.service

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.tregubowww.data.network.model.CharactersDto
import ru.tregubowww.data.network.model.CharactersDtoResult
import ru.tregubowww.data.network.model.EpisodeDtoResult

interface CharacterService {

    @GET("character/")
    suspend fun getCharacterList(@Query("page") query: Int): CharactersDto

    @GET("character/")
    suspend fun getCharactersFilter(
        @Query("name") name: String?,
        @Query("status") status: String?,
        @Query("species") species: String?,
        @Query("type") type: String?,
        @Query("gender") gender: String?,
        @Query("page") page: Int,
    ): CharactersDto

    @GET("character/")
    suspend fun getCharactersSearch(
        @Query("name") name: String,
        @Query("page") page: Int,
    ): CharactersDto

    @GET("character/{id}")
    suspend fun getCharacter(
        @Path("id") id: Long,
    ): CharactersDtoResult

    @GET("character/{id}")
    suspend fun getCharactersByListId(
        @Path("id") id: String,
    ): List<CharactersDtoResult>
}
