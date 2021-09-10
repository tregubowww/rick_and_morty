package ru.tregubowww.domain.repository

import androidx.paging.PagingSource
import ru.tregubowww.domain.entity.Character
import ru.tregubowww.domain.entity.Episode
import ru.tregubowww.domain.query.FilterQueryCharacter

interface CharacterRepository {
    fun getCharacters(): PagingSource<Int, Character>
    fun getSearchCharacter(query: String): PagingSource<Int, Character>
    suspend fun searchCharacterFromDb(querySearch: String): List<Character>
    fun getFilterCharacters(queryFilterCharacter: FilterQueryCharacter): PagingSource<Int, Character>

    suspend fun getCharacterFromNetwork(characterId: Long): Character
    suspend fun getCharacterFromDb(characterId: Long): Character
    suspend fun getCharacterListFromDb(): List<Character>
    suspend fun getFilterCharactersFromDb(queryFilterCharacter: FilterQueryCharacter): List<Character>
    suspend fun getCharacterListById(charactersId: String): List<Character>
    suspend fun getCharacterListByIdFromDb(charactersId: String): List<Character>
}
