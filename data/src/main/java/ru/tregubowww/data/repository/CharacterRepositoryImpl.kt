package ru.tregubowww.data.repository

import androidx.paging.PagingSource
import ru.tregubowww.data.db.dao.CharacterDao
import ru.tregubowww.data.network.service.CharacterService
import ru.tregubowww.data.paging_source.characters.CharacterPagingSource
import ru.tregubowww.data.paging_source.characters.FilterCharacterPagingSource
import ru.tregubowww.data.paging_source.characters.SearchCharacterPagingSource
import ru.tregubowww.data.toCharacter
import ru.tregubowww.data.toEpisode
import javax.inject.Inject
import ru.tregubowww.domain.entity.Character
import ru.tregubowww.domain.entity.Episode
import ru.tregubowww.domain.query.FilterQueryCharacter
import ru.tregubowww.domain.repository.CharacterRepository

class CharacterRepositoryImpl @Inject constructor(
    private val searchCharacterPagingSource: SearchCharacterPagingSource.Factory,
    private val filterCharacterPagingSource: FilterCharacterPagingSource.Factory,
    private val characterDao: CharacterDao,
    private val characterService: CharacterService,
) : CharacterRepository {

    override fun getCharacters(): PagingSource<Int, Character> =
        CharacterPagingSource(characterService, characterDao)

    override fun getSearchCharacter(query: String): PagingSource<Int, Character> =
        searchCharacterPagingSource.create(query)

    override fun getFilterCharacters(queryFilterCharacter: FilterQueryCharacter): PagingSource<Int, Character> =
        filterCharacterPagingSource.create(queryFilterCharacter)

    override suspend fun searchCharacterFromDb(querySearch: String): List<Character> =
        characterDao.searchCharacter("%${querySearch}%")

    override suspend fun getCharacterFromNetwork(characterId: Long): Character =
        characterService.getCharacter(characterId).toCharacter()

    override suspend fun getCharacterFromDb(characterId: Long): Character =
        characterDao.getCharacter(characterId)

    override suspend fun getCharacterListFromDb(): List<Character> =
        characterDao.getListCharacter()

    override suspend fun getFilterCharactersFromDb(queryFilterCharacter: FilterQueryCharacter): List<Character> =
        characterDao.filterCharacter(
            name = "%${queryFilterCharacter.name ?: ""}%",
            status = queryFilterCharacter.status ?: "%",
            species = queryFilterCharacter.species ?: "%",
            type = "%${queryFilterCharacter.type ?: ""}%",
            gender = queryFilterCharacter.gender ?: "%"
        )

    override suspend fun getCharacterListById(charactersId: String): List<Character> {
        return if (charactersId.split(", ").size == 1) listOf(characterService.getCharacter(charactersId.toLong()).toCharacter())
        else characterService.getCharactersByListId(charactersId).map { it.toCharacter() }
    }

    override suspend fun getCharacterListByIdFromDb(charactersId: String): List<Character> {
        val charactersIdList: List<String> = charactersId.split(", ")
        val characterList = mutableListOf<Character>()
        charactersIdList.forEach { if (it.isNotBlank()) characterList.add(characterDao.getCharacter( it.toLong())) }
        return characterList
    }
}